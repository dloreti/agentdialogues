# agentdialogues

Argumentative agents interacting to solve the Bat & Ball problem:

"A bat and a ball cost $1.10 in total. The bat costs $1 more than the ball. How much does the ball cost? ___ cents."

### Before downloading/cloning

agentdialogues uses the project [asp4j](https://github.com/hbeck/asp4j) as a java interface to [clingo](http://potassco.sourceforge.net).

Therefore, you first need to download and install clingo. 

Then, make the `clingo` command globally available e.g., by creating a link to the executable in a folder that is inside the `PATH` variable:
```
ln -s /path/to/clingo-<version-system>/clingo /usr/local/bin/clingo
```
  
Ensure you can execute clingo by typing `clingo -v`. This should give you an output like this:

```
$ clingo -v 
clingo version 5.2.2
Address model: 64-bit

libgringo version 5.2.2
Configuration: without Python, with Lua 5.3.4

libclasp version 3.3.3 (libpotassco version 1.0.1)
Configuration: WITH_THREADS=1
Copyright (C) Benjamin Kaufmann

License: The MIT License <https://opensource.org/licenses/MIT>
```

#### Eclispe and MacOS users

If you are using eclipse on MacOS, ensure to perform the following steps to have the same environment variables of the system available in the eclipse environment:

1. create a link to the eclipse executable and place it wherever you like it e.g.,
```
ln -s /Applications/eclipse/Eclipse.app/Contents/MacOS/eclipse /Applications/eclipse/
```
2. run eclipse by double clicking on the newly created link. This ensures that the `PATH` variable inside eclipse is the same as the system's one. So that, if can run `clingo` from terminal, you can do it from inside the asp4j project running in eclipse as well.

### Download or clone this project
After downloading/cloning this project, you should find the asp4j sources in the lib folder as a git submodule. 

You can run the example in MainBB, which loads the **Answer Set Programming (ASP)** specification of the Bat & Ball problem in the `src/main/resources/batball.lp` resource file and simulates the interaction between tree types of agents:
* X : the agent who gives the intuitive wrong answer 10c
* Y : the agent who has understood the problem and gives the counterintuitive right answer 5c 
* Z : the agent who gives the counterintuitive right answer 5c without having really understood the problem

