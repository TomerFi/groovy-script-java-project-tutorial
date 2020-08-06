---
title: Groovying with Java
published: false
description: Using Groovy scripts in a Java project tutorial
tags: ["java", "groovy", "tutorial", "code"]
---

## Groovying with Java

If you haven't combined [Groovy](https://groovy-lang.org/) scripts with your [Java](https://www.java.com/)
projects yet...

You're missing out...</br>
It's super easy with [Maven](https://maven.apache.org/),</br>
and it'll make your code more modular and elegant.

But most importantly,</br>
you'll have some fun scripting and if nothing else,</br>
you'll most certainly benefit from some of the *grooviest* features,</br>
including the magic that is [Groovy's GString](https://groovy-lang.org/syntax.html#all-strings)!</br>
:smiley:

You can check out the code for this tutorial in [Github][0].

## The given environment

Let's say you have a service for connecting customers to representatives via some sort of messaging service.

When the customer first reaches out to the service,</br>
he should be responded with a standard message that includes his name,
his queue number, and an informational greeting of some sort.

We have a couple of ways to accomplish that in *Java*.</br>

### Old-school: method invoking

First, we can use a basic method that takes the *name* and *queue number* as arguments:

```java
private static String createMessage(final String name, final int queueNum) {
  return String.format(
      "Hello %s, you're number %d, please wait patiently, here is some info:\n" +
      "Anim incididunt deserunt ex ad do aliquip.\n" +
      "Ullamco est Lorem nisi magna duis esse laboris commodo eu.\n" +
      "Anim laborum commodo sint est dolor veniam id non sint aliquip amet pariatur.\n" +
      "Ex non incididunt duis aliqua qui quis eiusmod ex eiusmod irure nisi sint anim.\n" +
      "Ipsum voluptate laboris eiusmod sint ea do.", name, queueNum);
}

public String getMessage(final String name, final int queueNum) {
  return createMessage(name, queueNum);
}
```

### New-school: bi-function applying

Well... It is **2020**...</br>
Let's write this as a *BiFunction*:

```java
private static final BiFunction<String, Integer, String> createMessage =
    (name, queueNum) -> String.format(
      "Hello %s, you're number %d, please wait patiently, here is some info:\n" +
      "Anim incididunt deserunt ex ad do aliquip.\n" +
      "Ullamco est Lorem nisi magna duis esse laboris commodo eu.\n" +
      "Anim laborum commodo sint est dolor veniam id non sint aliquip amet pariatur.\n" +
      "Ex non incididunt duis aliqua qui quis eiusmod ex eiusmod irure nisi sint anim.\n" +
      "Ipsum voluptate laboris eiusmod sint ea do.", name, queueNum);

public String getMessage(final String name, final int queueNum) {
  return createMessage.apply(name, queueNum);
}
```

### Even better: function currying

If we'll ever need to add a couple of more arguments to the mix,</br>
let's say an eta...</br>
We'll have to think out of the box here,</br>
maybe use a `POJO` as the argument or use [javatupels](https://www.javatuples.org/).</br>
Maybe create a `TriFunction` interface...

There are numerous possibilities.</br>
For instance, we can switch to *function currying*.</br>
It will make it easier to add arguments later on,</br>
and... well...</br>
It's fun invoking currying functions, am I right? :grin:

```java
private static final Function<String, Function<Integer, String>> createMessage =
    name -> queueNum -> String.format(
      "Hello %s, you're number %d, please wait patiently, here is some info:\n" +
      "Anim incididunt deserunt ex ad do aliquip.\n" +
      "Ullamco est Lorem nisi magna duis esse laboris commodo eu.\n" +
      "Anim laborum commodo sint est dolor veniam id non sint aliquip amet pariatur.\n" +
      "Ex non incididunt duis aliqua qui quis eiusmod ex eiusmod irure nisi sint anim.\n" +
      "Ipsum voluptate laboris eiusmod sint ea do.", name, queueNum);

public String getMessage(final String name, final int queueNum) {
  return createMessage.apply(name).apply(queueNum);
}
```

### Last but not least: groovy scripting

If you're like me,</br>
and you just hate seeing big long constant strings in your code.</br>
Then let's try this as a `Groovy Script` with a script called `create_message.groovy`:

```groovy
def name = bindName
def queueNum = bindQueueNum

"""Hello ${name}, you're number ${queueNum}, please wait patiently, here is some info:
Anim incididunt deserunt ex ad do aliquip.
Ullamco est Lorem nisi magna duis esse laboris commodo eu.
Anim laborum commodo sint est dolor veniam id non sint aliquip amet pariatur.
Ex non incididunt duis aliqua qui quis eiusmod ex eiusmod irure nisi sint anim.
Ipsum voluptate laboris eiusmod sint ea do."""
```

Please note:

- The `def` statements allow us to bind arguments from the shell.
- The `"""` marks the text as a `GString`, which allows us, in this case to:
  - Easily incorporate the arguments with no `String.format` required.
  - Produce multi-line strings without worrying about line breaks and long lines.
- What you might have noticed missing here, is the `return` statement.</br>
  With *Groovy* the last statement **is** the return statement.

Now let's invoke the script with some intentionally boiler plated *Java* code:

```java
public String getMessage(final String name, final int queueNum) {
  try {
    var shell = new GroovyShell();
    var scriptFile = new File(shell.getClassLoader().getResource("scripts/create_message.groovy").getFile());

    var script = shell.parse(scriptFile);

    var binding = new Binding();
    binding.setProperty("bindName", name);
    binding.setProperty("bindQueueNum", queueNum);

    script.setBinding(binding);

    return script.run().toString();
  } catch (IOException exc) {
    return exc.getMessage();
  }
}
```

:boom:</br>
Can you see how easy it is to add arguments?</br>
Just add them to the script, and add the binding to the invoking class.</br>
No more "scratching your head" when it comes to concatenating long strings...</br>
:relieved:

The real conundrum here is,</br>
How do we get the script in our class loader :question:

It's easy with *Maven*, let's say this is our project layout:

```text
- project
  - src
    - main
      - *.java
    - scripts
      - create_message.groovy
    - test
      - *Test.java
```

First, we need to include the [groovy dependency](https://mvnrepository.com/artifact/org.codehaus.groovy/groovy).</br>
(Note the version, which was the latest when this tutorial was written).

```xml
<dependencies>
    <dependency>
        <groupId>org.codehaus.groovy</groupId>
        <artifactId>groovy</artifactId>
        <version>3.0.5</version>
    </dependency>
</dependencies>
```

Now we have the appropriate *Groovy* API classes like *GroovyShell* and *Binding* in our class loader.
We now need to add the following to our `build` section in our `pom.xml`:

```xml
<build>
    <resources>
      <resource>
        <directory>src/scripts</directory>
        <targetPath>scripts</targetPath>
      </resource>
    </resources>
</build>
```

This will add everything from our `src/scripts` folder to our class loader under the folder `scripts`.</br>
So we'll be able to find our script like so: `scripts/create_message.groovy`.

That's it!

You can check out the code for this tutorial in [Github][0].

**:wave: See you in the next tutorial :wave:**

[0]: https://github.com/TomerFi/groovy-script-java-project-tutorial
