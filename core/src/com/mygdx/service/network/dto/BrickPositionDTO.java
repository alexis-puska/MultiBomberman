package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.domain.level.Level;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;
import com.mygdx.service.network.utils.NetworkRequestUtils;

public class BrickPositionDTO {

	private byte[] grid;

	public BrickPositionDTO(Level level) {
		grid = new byte[NetworkRequestUtils.calcGridSizeArray()];
		level.getBricks().stream().filter(b -> !b.isBurningOrBurned()).forEach(b -> {
			int val = b.getGridIndex() % 8;
			int index = Math.floorDiv(b.getGridIndex(), 8);
			switch (val) {
			case 0:
				grid[index] += 0b10000000;
				break;
			case 1:
				grid[index] += 0b01000000;
				break;
			case 2:
				grid[index] += 0b00100000;
				break;
			case 3:
				grid[index] += 0b00010000;
				break;
			case 4:
				grid[index] += 0b00001000;
				break;
			case 5:
				grid[index] += 0b00000100;
				break;
			case 6:
				grid[index] += 0b00000010;
				break;
			case 7:
			default:
				grid[index] += 0b00000001;
				break;
			}
		});
	}

	public BrickPositionDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		int lengthToCopy = NetworkRequestUtils.calcGridSizeArray();
		grid = new byte[lengthToCopy];
		bb.get(grid, 0, lengthToCopy);
		for (int i = 0; i < lengthToCopy; i++) {
			for (int j = 0; j < 8; j++) {

			}
		}
	}

	public boolean hasBrick(int gridIndex) {
		int val = gridIndex % 8;
		int index = Math.floorDiv(gridIndex, 8);
		switch (val) {
		case 0:
			return (grid[index] & 0b10000000) != 0;
		case 1:
			return (grid[index] & 0b01000000) != 0;
		case 2:
			return (grid[index] & 0b00100000) != 0;
		case 3:
			return (grid[index] & 0b00010000) != 0;
		case 4:
			return (grid[index] & 0b00001000) != 0;
		case 5:
			return (grid[index] & 0b00000100) != 0;
		case 6:
			return (grid[index] & 0b00000010) != 0;
		case 7:
		default:
			return (grid[index] & 0b00000001) != 0;
		}
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BRICK_POSITION.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BRICK_POSITION.ordinal());
		bb.put(grid);
		return bb.array();
	}

}
