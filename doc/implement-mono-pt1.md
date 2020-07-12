---
theme: gaia
_class: lead
paginate: true
backgroundColor: #092224

---

# **Demystify Project Reactor**

**Implement our own Mono (Pt 1)**

**- Kevin Fan**

---

# Project Reactor - The what

> Reactor is a fourth-generation reactive library, based on the **Reactive Streams specification**, for building non-blocking applications on the JVM

Reactor is a implementation of **Reactive Streams specification**

<https://projectreactor.io/docs/core/release/reference/>

---

# Reactive Streams Specification

Reactive Streams is a standard and specification for Stream-oriented libraries for the JVM that

- process a **potentially unbounded** number of elements
- **in sequence**,
- **asynchronously** passing elements between components,
- with mandatory **non-blocking backpressure**.

<https://www.reactive-streams.org/>

---

# Reactive Streams Specification - APIs

- Publisher
- Subscriber
- Subscription
- Processor

**NOTE**: These APIs are introduced to JDK 9, available in [java.util.concurrent.Flow](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html) 

<https://github.com/reactive-streams/reactive-streams-jvm/blob/v1.0.3/README.md>

---

# Demo

Reactive Streams APIs

---

# Publisher in Project Reactor

- **Mono**: emits at most one item 
- **Flux**: emits 0 to N items

---

# Agenda

Lets implement our own `Mono`, lets call that **Monoo**

---

# **Demo** 

```
Monoo.just(T value)
```

---

# Why I never use Subscription.request


Project reacotor provides you these APIs

```
subscribe(); 

subscribe(Consumer<? super T> consumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer,
          Consumer<? super Subscription> subscriptionConsumer); 
```

---

# **Demo** 

```
Monoo.subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer); 
```

---

# Mono - Operators

- A fake example

```
       Mono.just(1)
            .map(String::valueOf)
            .map(this::callHttpServiceAsync)
            .publishOn(Schedulers.boundedElastic())
            .filter(r -> !r.isEmpty())
            .doOnNext(this::logResponse)
            .doOnError(this::convertError)
            .block();
```

`map, filter, ...` are called operators

---

# **Demo** 

```
Monoo.map(Function<T, O> mapper)
```

---

# **Demo** 

```
Monoo.block()
```

---

# **Demo** 

```
Monoo.filter()
```

---

# Summary - Lifecyle of a Publisher

- Assemling (eg, Monoo.just.map.filter.....) - Flow/Stream pipeline
- Subscription (eg, Publisher.subsribe) 
- Runtime (Subscription.request|cancel + Subscriber.onNext/onError|onComplete)

---

# Summary

![](flow.png)

---

# Code on github
<https://github.com/kevinjom/reactive-streams-example>

## Homework

Clone the code and try to implement below operators

- doOnNext
- doOnError

---

# Potential follow-up sessions

- Concurrency models in Java (JDK builtin constructs)
- Implement more complex operators of project reactor (multithreaded environment)
- Concurrency models in Java (thrid party implementations)

---

# **Thank you**