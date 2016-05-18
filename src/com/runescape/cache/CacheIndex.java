package com.runescape.cache;

public enum CacheIndex {
      
      MODEL(1),
      
      ANIMATION(2),
      
      MUSIC(3),
      
      MAP(4);
      
      private int index;
      
      private CacheIndex(int index) {
            this.index = index;
      }

      public int getIndex() {
            return this.index;
      }     

}
