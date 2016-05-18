package com.runescape.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.runescape.Client;
import com.runescape.sign.SignLink;
import com.runescape.util.FileUtils;

public final class CacheUtils {

      public static void repackCacheIndex(Client client, CacheIndex cacheIndex) {
            System.out.println("Started repacking index " + cacheIndex.getIndex() + ".");
            int indexLength = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles().length;
            File[] file = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles();
            try {
                  for (int index = 0; index < indexLength; index++) {
                        int fileIndex = Integer.parseInt(
                                    Client.getFileNameWithoutExtension(file[index].toString()));
                        byte[] data = FileUtils.fileToByteArray(cacheIndex.getIndex(), fileIndex);
                        if (data != null && data.length > 0) {
                              client.indices[cacheIndex.getIndex()].method234(data.length, data, fileIndex);
                              System.out.println("Repacked " + fileIndex + ".");
                        } else {
                              System.out.println("Unable to locate index " + fileIndex + ".");
                        }
                  }
            } catch (Exception ex) {                  
                  System.out.println("Error packing cache index " + cacheIndex.getIndex() + ".");
            }
            System.out.println("Finished repacking " + cacheIndex.getIndex() + ".");
      }

      public static void dumpCacheIndex(Client client, CacheIndex cacheIndex) {
            System.out.println("Unpacking index" + cacheIndex.getIndex());
            try {
                  for (int i = 0;; i++) {
                        try {
                              byte[] indexByteArray = client.indices[cacheIndex.getIndex()].decompress(i);
                              if (indexByteArray == null) {
                                    System.out.println("Finished dumping index " + cacheIndex.getIndex()
                                                + ", exiting dump operation.");
                                    break;
                              }
                              BufferedOutputStream gzip = new BufferedOutputStream(
                                          new GZIPOutputStream(new FileOutputStream(
                                                      SignLink.findcachedir() + "dump" + cacheIndex.getIndex()
                                                                  + "/" + i + ".gz")));
                              if (indexByteArray.length == 0)
                                    continue;
                              else {
                                    gzip.write(indexByteArray);
                                    System.out.println("Unpacked " + i + ".");
                                    gzip.close();

                              }
                        } catch (IOException ex) {                              
                              throw new IOException(
                                          "Error writing to folder. Ensure you have this directory created: '"
                                                      + SignLink.findcachedir() + "dump"
                                                      + cacheIndex.getIndex() + "'");
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

}
