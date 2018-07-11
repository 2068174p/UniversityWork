Specifications - level 1
- You should model the intersection as a N x M grid (N does not necessarily equal
M)..i.e. N East-West lanes and M North-South lanes.
- Vehicles move either from North to South or from West to East.
- Only one vehicle can be in any one grid square. You should enforce this with a
ReentrantLock (and a condition).
- If a vehicle wants to move into an occupied position it should stop and wait for the
position to become free.
If uninterrupted, vehicles should move at their own constant speed (you should
model this by having a fixed delay once entering a position before attempting to
move into the next one). Each vehicle should have a randomly allocated constant
speed.
- Vehicles should be randomly generated to start in either the first row (North-South
vehicles) or first column (East-West vehicles) and each should exist on its own
thread. When a vehicle gets to the opposite edge it ‘falls off’.
- A separate object (on its own thread) should draw the grid (including its contents)
every 20 milliseconds (see example below)
- North to South vehicles should be drawn with a different character than West to East
ones.
- The simulator should stop after the grid has been drawn z (e.g. 2000) times.
- In your main method, you should make an example grid with ten rows and twenty
columns (ten and twenty lane roads are a bit unrealistic but it makes the plotting
more fun).

Specifications - level 2
- Different traffic generators should be able to be applied to different rows / columns.
Extend your code to allow for this, implementing at least two different generators
(perhaps they generate traffic with a different distribution of times, or traffic with a
different distribution of speeds). Note that you’re not being tested here on your ability
to implement different random generators but instead on your ability to make modular
code that allows for different traffic generator classes that adhere to some common
design.
- Extend your code to have lanes going in either direction. E.g. create a 10 row by 20
column demo where the top 5 rows go from left to right and the bottom five from right
to left. Similarly, the first ten columns go from top to bottom and the second ten from
bottom to top. Marks will be awarded here for neatness of the solution (i.e. I can
imagine a solution that has lots and lots of ‘if’ statements somewhere and that is not
what I’m after).
- Create the capability for each generator to report the time it takes all vehicles it
generates to travel through the grid. Once the simulator has finished, a statistics
class should be able to create a report that gives the maximum, minimum, mean and
variance times for each generator.