**[Why](#why) |**
**[How](#how) |**
**[What](#what) |**
**[Key Experiments](#key-experiments)**

# wAlnut

"*Perfect lets you stall, ask more questions, do more reviews, safe it up and
generally avoid doing anything that might fail or anything important. You're
not in the perfect business. Stop pretending that's what the world wants from
you.*"  
~ [Seth Godin](http://sethgodin.typepad.com/seths_blog/2015/06/abandoning-perfection.html)

[![Build Status](https://travis-ci.org/WalnutiQ/wAlnut.svg?branch=master)](https://travis-ci.org/WalnutiQ/wAlnut)

## Why

The long term goal of this repository is to store code that can simulate a full
sized human brain in real time. The current short term goal is to experiment
with a simplified visual pathway from an eye(that can move within a "room") to
algorithms that model hierarchical regions in layer 2/3, 4, & 5 of the neocortex
(70+% of the brain) to understand how a common learning algorithm needs to
work.

If we figure out the important biological computation principles behind the
human brain we will be able to simulate the intelligence of 1 brain on a
computer. This can be scaled to surpass the collective intelligence of the 7+
billion human brains on Earth.

I believe that an intelligent machine will be able to solve many of our
hardest problems and will cause many new hard problems. Technology has always
been used to do great good and great evil and it is scary to imagine the great
evil this technology brings. I desperately want to help build a technology that
solves our hardest problems where everything's happiness is considered.
Impossibilities of today like immortality, super intelligence, cure for cancer,
and the remaining secrets of our universe will be able to be discovered faster
by a general learning algorithm that doesn't need to eat, sleep, or reproduce.
And just as self driving cars will make human driving illegal any dangerous or
boring task will be replaced by a general learning algorithm. This will cause
massive job replacement; one of many of our new hard problems.

After we understood the principles of flight we were able to build planes that
are much faster and can carry more weight than birds. After we understand
the human neocortex's computational principles we will be able to run programs
much more intelligent than humans with unimaginable capabilities. At first it
will not have any emotions like you and I. This will be a good thing because it
will force us to ask if it would be moral to create an super intelligent machine
with emotions to help us. It may not necessarily be a good thing for the human
race. For example look at how humans treat [intelligent pigs](http://modernfarmer.com/2014/03/pigheaded-smart-swine/).
We are super intelligent emotional machines compared to pigs and the future
super intelligent emotional machines will be super intelligent compared to us.

This is just one possible route of how the future may play out out of many
options mostly unknown to us. I am most likely wrong. However, hopefully you
can see why I am so obsessed with making sure this project goes well.

If you are interested in becoming a researcher/developer I would be grateful
for your collaboration as I cannot do this alone. The <b>only</b> requirement
I ask for is that you are someone that is not "all talk" and understands that
progress is made by not making excuses. If you rely on habits and a "no excuses"
attitude please e-mail me at quinnliu@vt.edu to talk about how you can get
involved. This project has a long journey ahead so sustainability will take
precedence over crunch. Currently I have no funding for paying you.

This research was initially inspired by everyone at [Numenta](http://numenta.org/). 
Numenta has theorized and tested algorithms that model layers 2/3 & 4 of the 
human neocortex. They have generously released the pseudo code for their 
learning algorithms, and this repository is an extended implementation of 
their algorithms using object-oriented programming with a focus on 
<b>understandability</b> over speed and applications to <b>human vision</b>
using hierarchy and sensory motor integration. Numenta's implementation of their 
algorithms can be found [here](https://github.com/numenta/nupic). For more 
information please:

- Watch this [video playlist](http://www.youtube.com/playlist?list=PLPXsMt57rLtgddN0NQEmXP-FbF6wt2O-f)
  to become familiar with the neuroscience behind this repository.
- Read Numenta's great explanation of their research in this
  [white paper](https://github.com/WalnutiQ/papers/blob/master/HTM_CorticalLearningAlgorithms.pdf)
  to better understand the theory behind this repository.

~ Q

## How
Install all code with **[IntelliJ](#install-in-linuxmacwindows-with-intellij-recommended)**
(Recommended) or **[Gradle](#install-in-linuxmacwindows-with-gradle)** for 
Linux/Mac/Windows and then read **[How to contribute](#how-to-contribute)**.

#### Install in Linux/Mac/Windows with IntelliJ (Recommended)
1. If you have any problems with the following instructions please e-mail
   quinnliu@vt.edu and I will try to help the best I can. First
   make sure you have java version 1.8. To check open up a new terminal
   and type:
   ```sh
   prompt> java -version
   java version "1.8.0_60" # it's only important to see "1.8" in
                           # the version
   ```  
   If you don't have any of those Java versions install java 1.8 by going
   [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
   After installing java 1.8 open up a new terminal to check if java 1.8
   is installed by retyping `java -version` in the terminal.

2. Then [install IntelliJ IDEA FREE Community Edition](http://www.jetbrains.com/idea/download/).
     - Note where you choose to install this and choose a folder that is easy to access
       (I stored it in Documents)

3. Go to the top right of this page and hit the `Fork` button. Then clone your forked WalnutiQ
   repository locally.

4. Once IntelliJ is installed, open it.  Go to "File" `=>` "Import Project...". This should
   open up a new window and you should easily be able to select the "WalnutiQ" folder. Click "ok".

5. Now, the project should be imported.  You should now add JDK.  Go back to "File"
   `=>` "Project Structure".

6. A new window should popup.  Under "Platform Settings" `=>` "SDKs".  At the upper
   lefthand, select the "+" `=>` "JDK".

7. On Mac, In the "Finder" window, enter SHIFT + command + G.  Type in
   "/Library/Java/JavaVirtualMachines". Navigate to the JDK folder and once you
   have done so, click "choose". On Windows,
   navigate to the JDK folder in "C:\Program Files\Java". On Linux, good luck finding it.

8. If this was successfully done, a list of SDKs should now appear under SDKs.
   In the same window, navigate to the lefthand side and under "Project Settings" `=>`
   "Project".  Under "Project SDK", it should say "<No SDK>".  In that bar, select the new
   Java version which you added.  At the bottom righthand corner, select "Apply". Click
   "OK".

9. Now click "File" `=>` "New" `=>` "Project from Existing Sources..." `=>` Navigate to "WalnutiQ" folder
   and click "OK" `=>` Select "Gradle" `=>` Next `=>` Select "Use default gradle wrapper (recommended)" `=>` Finish

10. In the left side file viewer right-click the folder "WalnutiQ" and select `Run 'Tests in WalnutiQ''`. Hopefully
   they all pass.

#### Install in Linux/Mac/Windows with Gradle
1. If you have any problems with the following instructions please e-mail
   quinnliu@vt.edu and I will try to help the best I can. First
   make sure you have java version 1.8. To check open up a new terminal
   and type:
   ```sh
   prompt> java -version
   java version "1.8.0_60" # it's only important to see "1.8" in
                           # the version
   ```  
   If you don't have any of those Java versions install java 1.8 by going
   [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
   After installing java 1.8 open up a new terminal to check if java 1.8
   is installed by retyping `java -version` in the terminal.

2. Go to the top right of this page and hit the `Fork` button. Then clone your
   forked WalnutiQ repository locally. Navigate into the `WalnutiQ/` folder.

3. To run all of the code in the Linux or Mac terminal type:
   ```sh
   prompt> ./gradlew build
   # some other stuff...
   BUILD SUCCESSFUL # If you see `BUILD SUCCESSFUL` all of the tests have passed!
   ```

4. To run all of the code in the Windows terminal type:
   ```sh
   prompt> gradlew.bat
   # some other stuff...
   BUILD SUCCESSFUL # If you see `BUILD SUCCESSFUL` all of the tests have passed!
   ```

5. In the future after editing some code make sure to clean your old compiled
   code before rerunning the tests by typing:
   ```sh
   prompt> ./gradlew clean # removes your old compiled code
   prompt> ./gradlew build
   # hopefully all the tests still pass... :)
   ```

#### How to contribute
1. You need to be able to use Git & Github.com. If you don't know how I created
   a easy to follow 1.5 hour playlist on how to use Git & Github
   [here](https://www.youtube.com/watch?v=44E8o-xuxWo&list=PLPXsMt57rLtgpwFBqZq4QKxrD9Hhc_8L4).  

2. For now we are using the Git workflow model described
   [here](https://github.com/quinnliu/WalnutiQ/issues/62) to contribute to this
   repository effectively. Make sure you understand this model before
   trying to merge into the master branch. Additionally, before merging your
   feature branch to master make sure to change the quote at the top of this
   README.md to another quote.

3. View our [issue tracker](https://github.com/quinnliu/WalnutiQ/issues?state=open)
   and create a new issue with a question if you are confused. Otherwise,
   assign a issue to yourself you would like to work on or suggest
   a new issue if you kinda know what you are doing.

4. While reading through the code base it will be very helpful to refer to the
   following labeled model:
   ![alt text](https://raw.githubusercontent.com/WalnutiQ/artwork/master/MARK_I/labeled_MARK_I_version_2_high_contrast.jpg)

## What

If you are confused what a file is doing at a high level read
**[What are all the files here for?](#what-are-all-the-files-here-for)**. If
you want to learn about the most important brain theories we are using read
**[Important brain theories in use](#important-brain-theories-in-use)**. 

#### What are all the files here for
  - gradle/wrapper = the actual Gradle code for building our Java code
  - images = images used in training & testing the partial brain model
  - referencedLibraries = contains .jar files(of other people's code) needed to
                          run WalnutiQ
  - src
      + main/java/model
        - [MARK_II](./src/main/java/model/MARK_II) = the core logic for the
          partial brain model. Includes abstract data types for basic brain
          structures and learning algorithms that simulate how the brain learns.
          + [connectTypes](./src/main/java/model/MARK_II/connectTypes) =
            allow the different brain structures to connect to each other in
            a variety of ways
          + [generalAlgorithm](./src/main/java/model/MARK_II/generalAlgorithm)
            - [failureResearch](./src/main/java/model/MARK_II/generalAlgorithm/failureResearch)
              + temporalAlgorithms = Process of rethinking a prediction algorithm
                from the ground up using ideas from temporal pooler when
                necessary.
            - [SpatialPooler.java](./src/main/java/model/MARK_II/generalAlgorithm/SpatialPooler.java)
              = models the sparse & distributed spiking activity of neurons
              seen in the neocortex and models long term potentiation and
              depression on synapses of proximal dendrites
            - [TemporalPooler.java](./src/main/java/model/MARK_II/generalAlgorithm/TemporalPooler.java)
              = models neocortex's ability to predict future input using long
              term potentiation and depression on synapses of distal dendrites
          + [parameters](./src/main/java/model/MARK_II/parameters) = allows
            construction of different WalnutiQ models from command line for
            this repo https://github.com/quinnliu/CallWalnutiQ
          + region = components that make up a Region object
          + sensory = classes for allowing brain model to receive sensory input
          + unimplementedBiology = parts of the nervous system we haven't
            implemented into this model
        - [util](./src/main/java/model/util) = classes that enable the brain
          model properties to be viewed graphically and efficiently saved
          and opened  
      + test/java/model = test classes for important classes in the
                          `src/main/java/model` folder
        - experiments/vision = experiments with partial visual pathway models  
  - .gitignore = contains names of files/folders not to add to this repository
                 but keep in your local WalnutiQ folder
  - .travis.yml = tells [our custom travis testing site](https://travis-ci.org/quinnliu/WalnutiQ)
                  what versions of Java to test the files here
  - LICENSE.txt = GNU General Public License version 3
  - README.md = the file you are reading right now
  - build.gradle = compiles all of the code in this repository using Gradle
  - gradlew = allows you to use Gradle to run all of the code in this repository
              in Linux & Mac
  - gradlew.bat = allows you to use Gradle to run all of the code in this
                  repository in Windows

#### Important brain theories in use
1. **Theory:** 1 common learning algorithm in the neocortex of the brain
   - Supportive:
     + 1992 Paper [here](https://github.com/WalnutiQ/papers/blob/master/VisualProjectionsRouted.pdf)
       from Department of Brain and
       Cognitive Sciences at MIT.
       - Summary: A portion of wires of the optic nerve are routed to the auditory
         cortex in ferrets. The neurons of this primary auditory cortex(A1) with
         vision input did not work exactly like neurons in primary visual
         cortex(V1). Neurons in rewired A1 had larger receptive field sizes
         and other differences. However there are also similarities including:
         + rewired A1 neurons showed orientation and direction selectivity.
         + similar proportions of simple, complex, and nonoriented cells between
           rewired A1 and V1.
         + implies "*significant implications for possible commonalities in
           intracortical processing circuits between sensory cortices*".

     + 1988 Paper [here](https://github.com/WalnutiQ/papers/blob/master/VisualResponses.pdf)
       from Laboratoire des Neuroscience de la Vision at Universite de Paris.
       - Summary: Wires of the optic nerve were routed permanently to the
         main thalamic somatosensory nucleus in hamsters. After the hamsters
         grew up the neurons in the somatosensory cortex were recorded.
         The "*somatosensory neurons responded to visual stimulation of
         distinct receptive fields, and their response properties resembled,
         in several characteristic features, those of normal visual cortical
         neurons.*"
         + "*the same functional categories of neurons occurred in similar
           proportions, and the neurons' selectivity for the orientation or
           direction of movement of visual stimuli was comparable*" between
           normal hamsters and rewired hamsters.
         + "*These results suggest that thalamic nuclei or cortical areas at
           corresponding levels in the visual and somatosensory pathways perform
           similar transformations on their inputs*".

   - Not supportive:
     + 2002 Paper [here](https://github.com/WalnutiQ/papers/blob/master/CorticalHeterogeneity.pdf)
       from Vision, Touch and Hearing Research Centre at The University of Queensland.
          - "*recent studies have revealed substantial variation in pyramidal
            cell structure in different cortical areas*".
          - Some of these variations like increase in dendritic arbor size(
            dendritic branching) can be resolved with the idea of a common
            algorithm.

     + 2008 PhD thesis [here](https://github.com/WalnutiQ/papers/blob/master/HowTheBrainMightWork.pdf)
       by Dileep George from Stanford University
          - "*Until we understand and explain the computational reason behind
            a large majority of such variations, the common cortical algorithm
            will have to be considered as a working hypothesis*".

   - Conclusion: If cortex X(arbitrary cortex) of the neocortex(contains visual cortex,
     auditory cortex, somatosensory cortex, and others..) can be given
     non-normal sensory input usually given to say cortex Y and then learn to
     process this new input similarily to how cortex X would process it, then we
     can hypothesize that there is a common learning/predicting algorithm in all
     cortices of the neocortex.

2. **Theory:** The 1 common learning algorithm learns about the world by using
   when things happen across time. The algorithm makes no assumptions about
   the different transformations that our world has(shifts, rotations, etc.).
   The algorithm ONLY says if 1 pattern follows another in time in a
   predictable way, then they are causally related and should have the
   SAME REPRESENTATION in the brain.
   - Kinda Supportive:
     + 2008 PhD thesis [here](https://github.com/WalnutiQ/papers/blob/master/HowTheBrainMightWork.pdf)
       by Dileep George from Stanford University
       - There is a idea in optimization algorithms called No Free Lunch
         theorem. No Free Lunch theorem "state[s] that any two optimization algorithms
         are equivalent when their performance is averaged across all possible
         problems".
       - "*On the surface, the NFL theorem seems to create problems for the
         idea of a common cortical algorithm. How can one mechanism/algorithm
         do very well on tasks as different as vision, audition and language?
         The answer comes from the part of the NFL theorem that talks about
         the assumptions that need to be exploited ... If the cortex is good
         at learning a wide variety of tasks using a common mechanism, then
         there must be something common about these seemingly different tasks.*".

   - Not supportive:

   - No Conclusion Yet: Still experimenting with rethinking a prediction algorithm
     from the ground up using ideas from temporal pooler when necessary in folder [failureResearch](./src/main/java/model/MARK_II/generalAlgorithm/failureResearch).

# Key Experiments

The following experiments show off how a general learning algorithm uses the
brain's data structure to it's advantage to process input data and create 
intelligence. 

The first problem a general learning algorithm has is to not get confused by
all the noise in the world. The **[Noise invariance experiment](#noise-invariance-experiment)**
illustrates how the first part of the algorithm does not get confused by noisy 
input data. 

#### Noise invariance experiment

Here is some example code of how part of the theorized prediction algorithm
works. It is a beautiful summary of how columns of neurons in your neocortex
are probably working to encode what you see. The following are the three images
the retina will be looking at:

![alt text](https://raw.githubusercontent.com/WalnutiQ/artwork/master/presentations/three_images_of_digit_2.jpg)

```java
retina.seeBMPImage("2.bmp");
spatialPooler.performPooling();
// set1 = ((6,2), (1,5))
// (6,2) and (1,5) are the columns in the Region that are active after seeing
// "2.bmp"
assertEquals(set1, this.spatialPooler.getActiveColumnPositions());

retina.seeBMPImage("2_with_some_noise.bmp");
spatialPooler.performPooling();
// set1 = ((6,2), (1,5))
// NOTE: the columns that are active are still the same even though there was
//       quite a lot of noise in the input data
assertEquals(set1, this.spatialPooler.getActiveColumnPositions());

retina.seeBMPImage("2_with_a_lot_of_noise.bmp");
spatialPooler.performPooling();
// when there is a lot of noise notice how the active columns are
// no longer the same?
// set2 = ((6,2), (2,5))
assertEquals(set2, this.spatialPooler.getActiveColumnPositions());
```

You can view the entire file in
[NoiseInvarianceExperiment.java](./experiments/model/MARK_I/vision/NoiseInvarianceExperiment.java).
Please do not be afraid to ask a question if you are confused! This stuff took
me several months to fully understand but it is really beautiful after you
understand it.