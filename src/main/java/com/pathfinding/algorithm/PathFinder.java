package com.pathfinding.algorithm;

import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;


public interface PathFinder {

    Path findPath(PathRequest request);

    String getName();
}
