package io.emailthreading;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tianzhenjiu
 */
public class Message {

    Object message;
    String message_id;   // the ID of this message
    Set<String> references;
    String subject;


    public Message(String message_id, String subject, String... references) {
        this.message_id = message_id;
        this.references = new HashSet<>();
        if (references != null) {
            for (String ref : references) {
                this.references.add(ref);
            }
        }
        this.subject = subject;
    }

    /**
     * prune children and return new children list
     * @param container
     * @return
     */
    public static Set<Container> pruneContainer(Container container) {

        Set<Container> newChild = new HashSet();
        container.children.forEach(ctr -> {
            newChild.addAll(pruneContainer(ctr));
        });
        container.children.clear();

        newChild.forEach(c -> {
            container.addChild(c);
        });

        if (container.message == null &&
                container.children.isEmpty()) {
            return Collections.emptySet();
        } else if (container.message == null &&
                (container.children.size() == 1 || container.parent != null)) {
            Set<Container> L = new HashSet<>();
            L.addAll(container.children);
            container.children.clear();
            return L;
        } else {
            Set<Container> L = new HashSet<>();
            L.add(container);
            return L;
        }

    }

    /**
     * main threading method
     * @param messages
     * @return
     */
    public static Map<String, Container> thread(Message... messages) {

        Map<String, Container> idTable = new HashMap<>();
        for (Message msg : messages) {
            String messageId = msg.message_id;

            Container thisContainer = idTable.computeIfPresent(messageId, (k, container) -> {
                if (container.message == null) {
                    container.message = msg;
                }
                return container;
            });

            thisContainer = idTable.computeIfAbsent(messageId, (k) -> {
                Container container = new Container();
                container.message = msg;
                return container;
            });

            Container prev = null;
            Container container = null;
            for (String ref : msg.references) {
                if (!idTable.containsKey(ref)) {
                    container = new Container();
                    idTable.put(ref, container);
                } else {
                    container = idTable.get(ref);
                }

                if (prev != null) {
                    if (thisContainer == container) {
                        continue;
                    }
                    if (container.hasDescendant(prev)) {
                        continue;
                    }
                    prev.addChild(container);
                }
                prev = container;
            }
            if (prev != null) {
                prev.addChild(thisContainer);
            }
        }

        Set<Container> rootSet = idTable.values().stream().filter((container) -> {
            return container.parent == null;
        }).collect(Collectors.toSet());
        Set<Container> newRootSet = new HashSet<>();
        for (Container container : rootSet) {
            newRootSet.addAll(pruneContainer(container));
        }
        rootSet = newRootSet;

        Map<String, Container> subjectTable = new HashMap<>();
        for (Container container : rootSet) {
            String subject = null;
            if (container.message != null) {
                subject = container.message.subject;
            } else {
                subject = container.children.stream().findFirst().get().message.subject;
            }
            if (subject == null || subject.length() == 0) {
                continue;
            }
            Container existing = subjectTable.get(subject);
            if (existing == null || (
                    existing.message != null && container.message == null
            ) || (existing.message != null && container.message != null &&
                    existing.message.subject.length() > container.message.subject.length())
            ) {
                subjectTable.put(subject, container);
            }
        }

        for (Container container : rootSet) {
            String subject = null;
            if (container.message != null) {
                subject = container.message.subject;
            } else {
                subject = container.children.stream().findFirst().get().message.subject;
            }
            if (subject == null || subject.length() == 0) {
                continue;
            }
            Container ctr = subjectTable.get(subject);
            if (ctr == null || ctr == container) {
                continue;
            }

            if (ctr.isDummy() && container.isDummy()) {
                for (Container c : ctr.children) {
                    container.addChild(c);
                }
            } else if (ctr.isDummy() || container.isDummy()) {
                if (ctr.isDummy()) {
                    ctr.addChild(container);
                } else {
                    container.addChild(ctr);
                }
            } else if (ctr.message.subject.length() >= container.message.subject.length()) {
                ctr.addChild(container);
            } else {
                container.addChild(ctr);
            }

        }
        return subjectTable;
    }

    @Override
    public String toString() {
        return "Message{" +
                ", message_id='" + message_id + '\'' +
                ", references=" + references +
                ", subject='" + subject + '\'' +
                '}';
    }
}