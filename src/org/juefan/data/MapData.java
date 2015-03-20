package org.juefan.data;

import java.util.HashMap;
import java.util.Map;

public class MapData {
	public static final String DATA_SPLIP = "\001";
	public static final String MAP_SPLIT = ",";
	public static final String KV_SPLIT = ":";
	public int y;
	public Map<Integer, Integer> x = new HashMap<Integer, Integer>();

	public void setMap(String map) {
		String tmpString = map.substring(1, map.length() - 1).replace("\"", "");
		for (String KV : tmpString.split(MAP_SPLIT)) {
			String[] kvStrings = KV.split(KV_SPLIT);
			if (kvStrings.length == 2) {
				if (kvStrings[0].length() > 0 && !kvStrings[1].equals("0")) {
					x.put(Integer.parseInt(kvStrings[0]),
							Integer.parseInt(kvStrings[1]));
				}
			}
		}
	}

	public static void main(String[] args) {
		MapData mapData = new MapData();
		String test = "{\"112549\":\"36936\",\"112638\":\"270\",\"112662\":\"498\",\"113794\":\"968\"}";
		mapData.setMap(test);
		System.out.println(mapData.x);
	}
}
