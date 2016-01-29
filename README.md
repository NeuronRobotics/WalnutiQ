**[Why](#why) |**
**[How](#how) |**
**[What](#what) |**
**[Key Experiments](#key-experiments) |**
**[Team Bios](#team-bios) |**
**[Investors](#investors)**

# wAlnut

"*Why must our bodies break down and living cease? We have domesticated the 
entire planet to our whim. We have supercomputers that fit in our pockets, 
multiple on your person right now. We make space probes, engineered down to the 
micron, to send as far as Jupiter. We can make atomic bombs that can kill a 
great many of us at once.*"  
  
~ Cheryl Wu aka [grungerabbit](http://grungerabbit.com)

[![Build Status](https://travis-ci.org/WalnutiQ/wAlnut.svg?branch=master)](https://travis-ci.org/WalnutiQ/wAlnut)

## Why

I believe that intelligent machines will be able to solve many of our
hardest problems and will cause many new hard problems. Our hardest problems like
cancer, mortality, and secrets of the universe involve understanding a lot of 
data and as humans we are limited by the processing power and size of our brains. 
But if we figure out the computational principles that make you and I 
intelligent, simulate them on a computer, scale it up to surpass the collective 
intelligence of the 7+ billion people on Earth we will have created 
intelligent machines that do not need to eat, sleep, or reproduce that can
work on our hardest problems much more efficiently than humans. 

Technology has always been used to do great good and great evil and even if we 
have the best intentions to do good, intelligent machines will cause many new 
problems. For example there will be massive job loss since most jobs 
can be replaced by intelligent machines. A possible solution to this hard problem 
will be to augment our brains and become super intelligent. 

The long term goal of this repository is to store code that can simulate a full
sized human brain in real time. The current short term goal is to experiment
with a simplified visual pathway from an eye(that can move within a "room") to
algorithms that model hierarchical regions in layer 2/3, 4, & 5 of the neocortex
(70+% of the brain) to understand the principles of intelligence. 

After we understood the principles of flight we were able to build planes that
are much faster and can carry more weight than birds. After we understand
the human brain's computation principles we will be able to create intelligent
machines much more intelligent than humans with unimaginable capabilities. At 
first it will not have any emotions like you and I. This will be a good thing 
because it will force us to ask if it would be moral to create an super 
intelligent machine with emotions to help us. It may not necessarily be a 
good thing for the human race. For example look at how humans treat 
[intelligent pigs](http://modernfarmer.com/2014/03/pigheaded-smart-swine/).
We are more intelligent emotional machines compared to pigs and the future
super intelligent emotional machines will be much more intelligent compared to 
us.

If you are interested in becoming a researcher/developer I would be grateful
for your collaboration as I cannot do this alone. We currently have $$$
so read [how to get paid hourly rate to code wAlnut](https://github.com/WalnutiQ/wAlnut/wiki/How-to-get-paid-hourly-rate-to-code-wAlnut)
to get started. If you are interested in donating please read
[investors](#investors).

This research was initially inspired by everyone at [Numenta](http://numenta.org/).
Numenta has theorized and tested algorithms that model layers 2/3 & 4 of the
human neocortex(~70% of human brain). They generously released the pseudo code for their
learning algorithms, and this repository is an extended implementation of
their algorithms using object-oriented programming with a focus on
<b>understandability</b> over speed and applications to <b>human vision</b>
using hierarchy and sensory motor integration. Numenta's implementation of their
algorithms can be found [here](https://github.com/numenta/nupic). For more
information please:

- Watch this [video playlist](https://www.youtube.com/playlist?list=PLPXsMt57rLtgddN0NQEmXP-FbF6wt2O-f)
  to become familiar with the neuroscience behind this repository.
- Read Numenta's great explanation of their research in this
  [white paper](https://github.com/WalnutiQ/papers/blob/master/HTM_CorticalLearningAlgorithms.pdf)
  to better understand the theory behind this repository.

~ Q Liu

##Maven
```
<dependency>
  <groupId>com.neuronrobotics</groupId>
  <artifactId>WalnutiQ</artifactId>
  <version>2.3.2</version>
</dependency>
```
##Gradle

```
compile "com.neuronrobotics:WalnutiQ:2.3.2"
```
## How
Install all code with **[IntelliJ](#install-in-linuxmacwindows-with-intellij-recommended)**
(Recommended) or **[Gradle](#install-in-linuxmacwindows-with-gradle)** for
Linux/Mac/Windows and then read **[how to contribute](#how-to-contribute)**.

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
     - Note where you choose to install this and choose a folder that is easy to
       access (I stored it in Documents)

3. Open up IntelliJ and create a test project and get HelloWorld working.
   You will need to find your Java 1.8 jdk in the process. Once you have a program
   that can print something continue.

4. Go to the top right of this page and hit the `Fork` button. Then clone your forked wAlnut
   repository locally.

5. Inside IntelliJ, on Windows go to "File" `=>`
   "Import Project...". On Mac go to "File" `=>`
   "Project from Existing Sources...". This should open up a new window and you
   should easily be able to select the "wAlnut" folder. Click "OK".

6. Select "Import project from external model". Then select "Gradle" and hit
   "Next".

7. Select "Use default gradle wrapper (recommended)" `=>` hit Finish

8. In the left side file viewer right-click the folder "wAlnut" and select `Run 'Tests in wAlnut'`.
   Congrats! Go buy yourself some pistachio ice cream :)

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
   # Congrats! Go buy yourself some watermelon :)
   ```
   
   

#### How to contribute
1. You need to be able to use Git & Github.com. If you don't know how I created
   a easy to follow 1.5 hour playlist on how to use Git & Github
   [here](https://www.youtube.com/watch?v=44E8o-xuxWo&list=PLPXsMt57rLtgpwFBqZq4QKxrD9Hhc_8L4).  

2. For now we are using the Git workflow model described
   [here](https://github.com/WalnutiQ/wAlnut/issues/62) to contribute to this
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
**[what are all the files here for](#what-are-all-the-files-here-for)**. If
you want to learn about the most important brain theories we are using read
**[important brain theories in use](#important-brain-theories-in-use)**.

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
              + spatialAlgorithms = rethinking SDR algorithm to create different
                variations of the algorithms from the ground up using
                ideas from spatial pooler when necessary.
              + temporalAlgorithms = rethinking a prediction algorithm to create
                different variations of the algorithm from the ground up using
                ideas from temporal pooler when necessary.
            - [SpatialPooler.java](./src/main/java/model/MARK_II/generalAlgorithm/SpatialPooler.java)
              = models the sparse & distributed spiking activity of neurons
              seen in the neocortex and models long term potentiation and
              depression on synapses of proximal dendrites
            - [TemporalPooler.java](./src/main/java/model/MARK_II/generalAlgorithm/TemporalPooler.java)
              = models neocortex's ability to predict future input using long
              term potentiation and depression on synapses of distal dendrites
          + [parameters](./src/main/java/model/MARK_II/parameters) = allows
            construction of different WalnutiQ models from command line for
            this repo https://github.com/WalnutiQ/call_wAlnut
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
all the noise in the world. The **[noise invariance experiment](#noise-invariance-experiment)**
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

## Team Bios

#### Q's bio  
Hi my name is Q.

In 2011 I watched a talk about a guy trying to build a machine more intelligent
than humans and I fell in love with this idea. At the time I believed that an
intelligent machine would be able to solve many of our hardest problems(ex.
secrets of the universe and cure for cancer) and would cause many new hard
problems(ex. massive job loss). I was determined to use it to benefit humanity
as much as possible but first I needed to build it.

I told myself I would give myself 1 year(end of 2012) to build a machine more
intelligent than humans which I believed was plenty of time since I didn’t know
how to code yet. By 2013 2 years had gone by and although I had learned a lot of
interesting brain theories from neuroscientists and coding techniques from
computer scientists I gave up on this dream out of frustration on my speed of
progress and fell into a deep depression. Most of my identity came from the
brain research I did and I had hit a wall in the research I did not have the
confidence to break through.

After focusing on my physical health, family, and friends I eventually got out
of my depression and decided to restart working towards my dream of creating a
intelligent machine for the past year. However, this time I have learned to use habits
over motivation, collaboration over working alone, sustainability over crunch,
intense focus on what is necessary over nice to haves, acknowledgement you must
sometimes forget what you know in order to create something new, and many
beautiful ideas I have yet to learn.

Outside of AI I like to make YouTube videos, skateboard, watch rom coms, and
walk around manhattan eating pistachio ice cream and watermelon.

#### Aarathi's bio
Coming soon she says...

#### Manpreet's bio
Coming soon he says...

#### Kevins's bio
[Kevin Harrington](https://github.com/madhephaestus) is a java developer and roboticist based in Worcester MA. He brings robotics controls, high performance Java, and hardware integration skills to the project. He will be focused on sensor-motor interfaces and simulationg the robot for learning environments. 

#### Mark's bio
Hi, I'm Mark. I like math, sometimes.

#### Your bio
Interested in helping us code an intelligent machine? Read [how you can get paid an
hourly rate to code wAlnut](https://github.com/WalnutiQ/wAlnut/wiki/How-to-get-paid-hourly-rate-to-code-wAlnut)
to get started.

## Investors

Hi! My name is Q. To me a wAlnut investor is someone who believes we can build a 
machine more intelligent than humans and use it for more good out of love than 
evil out of fear. 

1. If you would like to become an investor of wAlnut simply venmo your donation.
In the `Search People` section type `@walnut` and look for the pink walnut logo.
It will look similar to this:
![alt text](https://raw.githubusercontent.com/WalnutiQ/artwork/master/logo/venmo_walnut_screenshot.png)

2. In the venmo `What's it for?` section
please provide the following information as you would like it to be displayed
in our [master investors list](#master-investors-list). Here is
an example of one of our investors:
![alt text](https://raw.githubusercontent.com/WalnutiQ/artwork/master/logo/venmo_payment_Walnut_iQ_screenshot.png)

3. If you would like to learn how your donation is being spent to pay developers
read [here](https://github.com/WalnutiQ/wAlnut/wiki/How-to-get-paid-hourly-rate-to-code-wAlnut).

4. We will periodically make youtube videos posted
[here](https://www.youtube.com/playlist?list=PLPXsMt57rLtgddN0NQEmXP-FbF6wt2O-f)
explaining our research. Care will be taken to ensure it is understandable to 
everyone including 10 year old kids with no neuroscience or coding background.

5. In the history of mankind some technologies are so powerful it would be wrong
to have it only accessible by a group of people. Examples include electricity,
computers, the internet, and now its a machine smarter than humans. Our latest 
research and code will always be available here for free to everyone in the world with 
a laptop, internet, and a little bit of curiosity.

## Master Investors List

Name | Amount (USD) | Favorite type of nut | website
-----|--------------|----------------------|---------
Q Liu            | $700.82 | Pistachio ice cream | [www.youtube.com/quinnliuvideos](www.youtube.com/quinnliuvideos)
Frederick Scholl | $10     | Unkown              | A Mystery
Perry Leong      | $5      | Cashews             | [linkedin.com/profile/view?id=183115052](linkedin.com/profile/view?id=183115052)
Ying Ying Wu     | $5      | Unkown              | A Mystery