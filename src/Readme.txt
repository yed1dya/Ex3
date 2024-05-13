Ex3 - 207404997

The general logic of the algorithm:
1. Find a target. 
2. Calculate the shortest path to the target.
3. Check the relation of x,y values of the current position and the next pixel in the path. Return the relevant direction needed to move to that pixel.

The algorithm decides the next target according to the following order:
1. checks which ghosts are active and finds the shortest path between the ghost and the pacman. The length of the path is the distance to the ghost.
If a ghost is closer than 5 steps, this activates the "ghostIsClose" case:

2. Within the "ghostIsClose" case, check how much time is left to eat the ghost: 
 2a. If it's less than 1 second (2 seconds in scenario 4), then the ghost is a threat. Calculate the point that is furthest from the dangerous ghosts (only the ones within range 5) and return that point as the target.
 2b. If there is more than 1 second (2 seconds in scenario 4), then return that ghost's position as the target.

3. If no ghosts are close, find the closest food and return that point as the target.


