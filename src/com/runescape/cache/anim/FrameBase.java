package com.runescape.cache.anim;
import com.runescape.io.Buffer;

public final class FrameBase {
      
      /**
       * The type of each transformation.
       */
      public final int[] transformationType;
      
      
      public final int[][] labels;

	public FrameBase(Buffer stream) {	      
		int count = stream.readUnsignedByte();
		transformationType = new int[count];	
		
		labels = new int[count][];		
		for (int index = 0; index < count; index++) {		      
			transformationType[index] = stream.readUnsignedByte();
		}

		for (int label = 0; label < count; label++) {		      
			labels[label] = new int[stream.readUnsignedByte()];
		}

		for (int label = 0; label < count; label++) {     
			for (int index = 0; index < labels[label].length; index++) {			      
				labels[label][index] = stream.readUnsignedByte();
			}
		}

	}

}
