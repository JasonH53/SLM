package me.strafe.utils.pathfinding;

import me.strafe.utils.ChatUtils;
import me.strafe.utils.VecUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;

import static me.strafe.utils.Stolenutils.mc;

public class AStarCustomPathfinder {
    private Vec3 startVec3;
    private Vec3 endVec3;
    private ArrayList<Vec3> path = new ArrayList<>();
    private ArrayList<Hub> hubs = new ArrayList<>();
    private ArrayList<Hub> hubsToWork = new ArrayList<>();
    private double minDistanceSquared;
    private boolean nearest = true;

    private static final Vec3[] flatCardinalDirections = {
            new Vec3(1, 0, 0),
            new Vec3(-1, 0, 0),
            new Vec3(0, 0, 1),
            new Vec3(0, 0, -1)
    };

    public AStarCustomPathfinder(Vec3 startVec3, Vec3 endVec3, double minDistanceSquared) {
        this.startVec3 = VecUtils.floorVec(startVec3);
        this.endVec3 = VecUtils.floorVec(endVec3);
        this.minDistanceSquared = minDistanceSquared;
    }

    public ArrayList<Vec3> getPath() {
        return path;
    }

    public void compute() {
        compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        path.clear();
        hubsToWork.clear();
        ArrayList<Vec3> initPath = new ArrayList<>();
        initPath.add(startVec3);
        hubsToWork.add(new Hub(startVec3, null, initPath, startVec3.squareDistanceTo(endVec3), 0.0, 0.0));
        search:
        for (int i = 0; i < loops; ++i) {
            hubsToWork.sort(new CompareHub());
            int j = 0;
            if (hubsToWork.size() == 0) {
                break;
            }
            for (Hub hub : new ArrayList<>(hubsToWork)) {
                if (++j > depth) {
                    break;
                }

                hubsToWork.remove(hub);
                hubs.add(hub);

                for (Vec3 direction : flatCardinalDirections) {
                    Vec3 loc = VecUtils.ceilVec(hub.getLoc().add(direction));
                    if (checkPositionValidity(loc, true)) {
                        if (addHub(hub, loc, 0)) {
                            break search;
                        }
                    }
                }

                if(checkPositionValidity(hub.getLoc().addVector(0, 1, 0), false)) {
                    for (Vec3 direction : flatCardinalDirections) {
                        Vec3 loc = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, 1, 0));
                        if (checkPositionValidity(loc, true) && fenceCheck(loc)) {
                            if (addHub(hub, loc, 0)) {
                                break search;
                            }
                        }
                    }
                }

                for (Vec3 direction : flatCardinalDirections) {
                    for(int k = 0; k < 256; k++) {
                        Vec3 forward = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, -k, 0));
                        if (checkPositionValidity(forward, false)) {
                            Vec3 loc = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, -(k + 1), 0));
                            if (checkPositionValidity(loc, true)) {
                                if (addHub(hub, loc, 0)) {
                                    break search;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (nearest) {
            hubs.sort(new CompareHub());
            path = hubs.get(0).getPath();
        }
    }

    public static boolean fenceCheck(Vec3 loc) {
        return fenceCheck((int) loc.xCoord, (int) loc.yCoord, (int) loc.zCoord);
    }

    public static boolean fenceCheck(int x, int y, int z) {
        BlockPos block1 = new BlockPos(x, y, z);
        return !isFence(block1);
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return checkPositionValidity((int) loc.xCoord, (int) loc.yCoord, (int) loc.zCoord, checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        if (isBlockSolid(block1)) return false;

        BlockPos block2 = new BlockPos(x, y + 1, z);
        if (isBlockSolid(block2)) return false;

        if (!checkGround) return true;

        BlockPos block3 = new BlockPos(x, y - 1, z);

        return isBlockSolid(block3) && isSafeToWalkOn(block3);
    }

    private static boolean isFence(BlockPos block) {
        IBlockState bs = mc.theWorld.getBlockState(block);
        Block b = bs.getBlock();
        return b instanceof BlockFence || b instanceof BlockWall;
    }

    private static boolean isBlockSolid(BlockPos block) {
        IBlockState bs = mc.theWorld.getBlockState(block);
        Block b = bs.getBlock();
        return mc.theWorld.isBlockFullCube(block) || b instanceof BlockSlab || b instanceof BlockStairs || b instanceof BlockCactus || b instanceof BlockChest || b instanceof BlockEnderChest || b instanceof BlockSkull || b instanceof BlockPane || b instanceof BlockFence || b instanceof BlockWall || b instanceof BlockGlass || b instanceof BlockPistonBase || b instanceof BlockPistonExtension || b instanceof BlockPistonMoving || b instanceof BlockStainedGlass || b instanceof BlockTrapDoor;
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        IBlockState bs = mc.theWorld.getBlockState(block);
        IBlockState above = mc.theWorld.getBlockState(block.add(0, 1, 0));
        Block b = bs.getBlock();
        if(above.getBlock() instanceof BlockLiquid) return false;
        return !(b instanceof BlockFence) && !(b instanceof BlockWall);
    }

    public Hub isHubExisting(Vec3 loc) {
        for (Hub hub : hubs) {
            if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord && hub.getLoc().zCoord == loc.zCoord) {
                return hub;
            }
        }
        for (Hub hub : hubsToWork) {
            if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord && hub.getLoc().zCoord == loc.zCoord) {
                return hub;
            }
        }
        return null;
    }

    public boolean addHub(Hub parent, Vec3 loc, double cost) {
        Hub existingHub = isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (existingHub == null) {
            if ((loc.xCoord == endVec3.xCoord && loc.yCoord == endVec3.yCoord && loc.zCoord == endVec3.zCoord) || (minDistanceSquared != 0.0 && loc.squareDistanceTo(endVec3) <= minDistanceSquared)) {
                path.clear();
                (path = parent.getPath()).add(loc);
                return true;
            }
            ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }

    private static class Hub {
        private Vec3 loc;
        private Hub parent;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3 loc, Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = null;
            this.parent = null;
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public Vec3 getLoc() {
            return loc;
        }

        public Hub getParent() {
            return parent;
        }

        public ArrayList<Vec3> getPath() {
            return path;
        }

        public double getSquareDistanceToFromTarget() {
            return squareDistanceToFromTarget;
        }

        public double getCost() {
            return cost;
        }

        public void setLoc(Vec3 loc) {
            this.loc = loc;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public void setPath(ArrayList<Vec3> path) {
            this.path = path;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public static class CompareHub implements Comparator<Hub> {
        @Override
        public int compare(Hub o1, Hub o2) {
            return (int) (o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }
}
