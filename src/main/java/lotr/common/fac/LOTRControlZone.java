package lotr.common.fac;

import lotr.common.world.map.LOTRWaypoint;

public class LOTRControlZone {
	public final int mapX;
	public final int mapY;
	public final int radius;
	public final int xCoord;
	public final int zCoord;
	public final int radiusCoord;
	public final long radiusCoordSq;

	public LOTRControlZone(int x, int y, int r) {
		mapX = x;
		mapY = y;
		radius = r;
		xCoord = LOTRWaypoint.mapToWorldX(mapX);
		zCoord = LOTRWaypoint.mapToWorldZ(mapY);
		radiusCoord = LOTRWaypoint.mapToWorldR(radius);
		radiusCoordSq = (long) radiusCoord * (long) radiusCoord;
	}

	public LOTRControlZone(LOTRWaypoint wp, int r) {
		this(wp.getX(), wp.getY(), r);
	}

	public boolean inZone(double x, double y, double z, int extraMapRange) {
		double dx = x - xCoord;
		double dz = z - zCoord;
		double distSq = dx * dx + dz * dz;
		if (extraMapRange == 0) {
			return distSq <= radiusCoordSq;
		}
		int checkRadius = LOTRWaypoint.mapToWorldR(radius + extraMapRange);
		long checkRadiusSq = (long) checkRadius * (long) checkRadius;
		return distSq <= checkRadiusSq;
	}

	public boolean intersectsWith(LOTRControlZone other, int extraMapRadius) {
		double dx = other.xCoord - xCoord;
		double dz = other.zCoord - zCoord;
		double distSq = dx * dx + dz * dz;
		double r12 = radiusCoord + other.radiusCoord + LOTRWaypoint.mapToWorldR(extraMapRadius * 2);
		double r12Sq = r12 * r12;
		return distSq <= r12Sq;
	}
}
