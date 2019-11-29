# Kroy

An 'Open Day' game aimed at prospective students and their parents visiting the University of York, built as part of the Year 2 Software Engineering Project (SEPR) module.

## Table of Contents

[TOC]

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You will need to install the Java Development Kit (JDK) version 1.8 to be able to build and run the project. You can [download the JDK for free from Oracle](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

If you don't already have a git client installed, you'll also need one of these. Console and GUI versions are available from the [git SCM website](https://git-scm.com).

### Installation

To create a copy of the Kroy project on your computer, navigate to the directory or folder you want to store the project, and then *clone* the Kroy Github project:

```
git clone https://github.com/ArchieGodfrey/Kroy.git
```

Then open the directory in an IDE or code editor, and make any changes. Remember to commit *little* and *often*.

#### Opening in Eclipse IDE

When you open Eclipse, you need to create a workspace directory. This cannot be the Kroy directory of the project.

To open the project:
- go to `File` > `Import` > `Gradle` > `Existing Gradle Project`
- set your local Kroy project folder as the `Project root directory`
- click `Finish` and do not configure any further settings

The project dependencies are given in `build.gradle` files. If you right-click on the `Package explorer` > `Gradle` > `Refresh Gradle Project` to rebuild the Eclipse build paths using the Gradle configurations.

### Configuration

#### Visual Studio Code

To be able to run tests in VSCode using the [Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test) extension, you will need to edit the `core/src/.classpath` file and add `<attribute name="test" value="true"/>` as in the example below:

```xml
<classpathentry kind="src" output="bin/main" path="src">
    <attributes>
    	<attribute name="test" value="true"/>
    </attributes>
</classpathentry>
```

## Tools

* [BadLogicGames libGDX](https://libgdx.badlogicgames.com) - The game frameworks and libraries used
* [Gradle](https://gradle.org) - A Java build tool
* [Tiled Map Editor](https://www.mapeditor.org) - To edit the tiled game map