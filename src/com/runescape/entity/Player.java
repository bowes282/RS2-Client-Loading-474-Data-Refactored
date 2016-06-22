package com.runescape.entity;

import com.runescape.Client;
import com.runescape.cache.anim.Animation;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.anim.Graphic;
import com.runescape.cache.def.ItemDefinition;
import com.runescape.cache.def.NpcDefinition;
import com.runescape.collection.ReferenceCache;
import com.runescape.entity.model.IdentityKit;
import com.runescape.entity.model.Model;
import com.runescape.io.Buffer;
import com.runescape.util.StringUtils;

public final class Player extends Mob {

      public String clanName = "None";

      public Model getRotatedModel() {
            if (!visible)
                  return null;
            Model model = method452();
            if (model == null)
                  return null;
            super.height = model.modelHeight;
            model.fits_on_single_square = true;
            if (aBoolean1699)
                  return model;
            if (super.graphic != -1 && super.currentAnimation != -1) {
                  Graphic spotAnim = Graphic.cache[super.graphic];
                  Model model_2 = spotAnim.getModel();
                  if (model_2 != null) {
                        Model model_3 = new Model(true, Frame.noAnimationInProgress(super.currentAnimation),
                                    false, model_2);
                        int nextFrame =
                                    spotAnim.animationSequence.primaryFrames[super.nextGraphicsAnimationFrame];
                        int cycle1 = spotAnim.animationSequence.durations[super.currentAnimation];
                        int cycle2 = super.anInt1522;
                        model_3.translate(0, -super.graphicHeight, 0);
                        model_3.skin();
                        model_3.method470(
                                    spotAnim.animationSequence.primaryFrames[super.currentAnimation],
                                    nextFrame, cycle1, cycle2);
                        model_3.faceGroups = null;
                        model_3.vertexGroups = null;
                        if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
                              model_3.scale(spotAnim.resizeXY, spotAnim.resizeXY, spotAnim.resizeZ);
                        model_3.light(64 + spotAnim.modelBrightness, 850 + spotAnim.modelShadow,
                                    -30, -50, -30, true);
                        Model aclass30_sub2_sub4_sub6_1s[] = {model, model_3};
                        model = new Model(aclass30_sub2_sub4_sub6_1s);
                  } else {
                        return null;
                  }
            }
            if (objectModel != null) {
                  if (Client.tick >= objectModelStop)
                        objectModel = null;
                  if (Client.tick >= objectModelStart && Client.tick < objectModelStop) {
                        Model model_1 = objectModel;
                        model_1.translate(objectXPos - super.x, objectCenterHeight - anInt1709,
                                    objectYPos - super.y);
                        if (super.nextStepOrientation == 512) {
                              model_1.method473();
                              model_1.method473();
                              model_1.method473();
                        } else if (super.nextStepOrientation == 1024) {
                              model_1.method473();
                              model_1.method473();
                        } else if (super.nextStepOrientation == 1536)
                              model_1.method473();
                        Model aclass30_sub2_sub4_sub6s[] = {model, model_1};
                        model = new Model(aclass30_sub2_sub4_sub6s);
                        if (super.nextStepOrientation == 512)
                              model_1.method473();
                        else if (super.nextStepOrientation == 1024) {
                              model_1.method473();
                              model_1.method473();
                        } else if (super.nextStepOrientation == 1536) {
                              model_1.method473();
                              model_1.method473();
                              model_1.method473();
                        }
                        model_1.translate(super.x - objectXPos, anInt1709 - objectCenterHeight,
                                    super.y - objectYPos);
                  }
            }
            model.fits_on_single_square = true;
            return model;
      }

      public void updateAppearance(Buffer stream) {
            stream.currentPosition = 0;
            gender = stream.readUnsignedByte();
            headIcon = stream.readUnsignedByte();
            skullIcon = stream.readUnsignedByte();
            hintIcon = stream.readUnsignedByte();
            desc = null;
            team = 0;
            
            for (int bodyPart = 0; bodyPart < 12; bodyPart++) {

                  int reset = stream.readUnsignedByte();

                  if (reset == 0) {
                        equipment[bodyPart] = 0;
                        continue;
                  }
                  
                  int id = stream.readUnsignedByte();
                  
                  equipment[bodyPart] = (reset << 8) + id;
                  
                  if (bodyPart == 0 && equipment[0] == 65535) {
                        desc = NpcDefinition.lookup(stream.readUShort());
                        break;
                  }                  
                  
                  if (equipment[bodyPart] >= 512 && equipment[bodyPart] - 512 < ItemDefinition.item_count) {
                        int team = ItemDefinition.lookup(equipment[bodyPart] - 512).team;
                        
                        if (team != 0) {
                              this.team = team;
                        }
                        
                  }
                  
            }

            for (int part = 0; part < 5; part++) {                    
                  int color = stream.readUnsignedByte();                   
                  if (color < 0 || color >= Client.PLAYER_BODY_RECOLOURS[part].length) {
                        color = 0;
                  }                  
                  appearanceColors[part] = color;                  
            }

            super.idleAnimation = stream.readUShort();
            if (super.idleAnimation == 65535) {
                  super.idleAnimation = -1;
            }
            
            super.standTurnAnimIndex = stream.readUShort();
            if (super.standTurnAnimIndex == 65535) {
                  super.standTurnAnimIndex = -1;
            }
            
            super.walkAnimIndex = stream.readUShort();
            if (super.walkAnimIndex == 65535) {
                  super.walkAnimIndex = -1;
            }
            
            super.turn180AnimIndex = stream.readUShort();
            if (super.turn180AnimIndex == 65535) {
                  super.turn180AnimIndex = -1;
            }
            
            super.turn90CWAnimIndex = stream.readUShort();
            if (super.turn90CWAnimIndex == 65535) {
                  super.turn90CWAnimIndex = -1;
            }
            
            super.turn90CCWAnimIndex = stream.readUShort();
            if (super.turn90CCWAnimIndex == 65535) {
                  super.turn90CCWAnimIndex = -1;
            }
            
            super.runAnimIndex = stream.readUShort();
            if (super.runAnimIndex == 65535) {
                  super.runAnimIndex = -1;
            }
            
            name = StringUtils.formatUsername(StringUtils.decodeBase37(stream.readLong()));
            combatLevel = stream.readUnsignedByte();
            skill = stream.readUShort();
            visible = true;
            aLong1718 = 0L;
            for (int k1 = 0; k1 < 12; k1++) {
                  aLong1718 <<= 4;
                  if (equipment[k1] >= 256)
                        aLong1718 += equipment[k1] - 256;
            }

            if (equipment[0] >= 256)
                  aLong1718 += equipment[0] - 256 >> 4;
            if (equipment[1] >= 256)
                  aLong1718 += equipment[1] - 256 >> 8;
            for (int i2 = 0; i2 < 5; i2++) {
                  aLong1718 <<= 3;
                  aLong1718 += appearanceColors[i2];
            }

            aLong1718 <<= 1;
            aLong1718 += gender;
      }

      public Model method452() {
            if (desc != null) {
                  int currentFrame = -1;
                  int nextFrame = -1;
                  int cycle1 = 0;
                  int cycle2 = 0;
                  if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
                        Animation animation = Animation.animations[super.emoteAnimation];
                        currentFrame = animation.primaryFrames[super.displayedEmoteFrames];
                        nextFrame = animation.primaryFrames[super.nextAnimationFrame];
                        cycle1 = animation.durations[super.displayedEmoteFrames];
                        cycle2 = super.emoteTimeRemaining;
                  } else if (super.movementAnimation >= 0) {
                        Animation animation = Animation.animations[super.movementAnimation];
                        currentFrame = animation.primaryFrames[super.displayedMovementFrames];
                        nextFrame = animation.primaryFrames[super.nextIdleAnimationFrame];
                        cycle1 = animation.durations[super.displayedMovementFrames];
                        cycle2 = super.anInt1519;
                  }
                  Model model = desc.method164(-1, currentFrame, null, nextFrame, cycle1, cycle2);
                  return model;
            }
            long l = aLong1718;
            int currentFrame = -1;
            int nextFrame = -1;
            int cycle1 = 0;
            int cycle2 = 0;
            int i1 = -1;
            int j1 = -1;
            int k1 = -1;
            if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
                  Animation animation = Animation.animations[super.emoteAnimation];
                  currentFrame = animation.primaryFrames[super.displayedEmoteFrames];
                  nextFrame = animation.primaryFrames[super.nextAnimationFrame];
                  cycle1 = animation.durations[super.displayedEmoteFrames];
                  cycle2 = super.emoteTimeRemaining;
                  if (super.movementAnimation >= 0
                              && super.movementAnimation != super.idleAnimation)
                        i1 = Animation.animations[super.movementAnimation].primaryFrames[super.displayedMovementFrames];
                  if (animation.playerOffhand >= 0) {
                        j1 = animation.playerOffhand;
                        l += j1 - equipment[5] << 40;
                  }
                  if (animation.playerMainhand >= 0) {
                        k1 = animation.playerMainhand;
                        l += k1 - equipment[3] << 48;
                  }
            } else if (super.movementAnimation >= 0) {
                  Animation animation = Animation.animations[super.movementAnimation];
                  currentFrame = animation.primaryFrames[super.displayedMovementFrames];
                  nextFrame = animation.primaryFrames[super.nextIdleAnimationFrame];
                  cycle1 = animation.durations[super.displayedMovementFrames];
                  cycle2 = super.anInt1519;
            }
            Model model_1 = (Model) models.get(l);
            if (model_1 == null) {
                  boolean flag = false;
                  for (int i2 = 0; i2 < 12; i2++) {
                        int k2 = equipment[i2];
                        if (k1 >= 0 && i2 == 3)
                              k2 = k1;
                        if (j1 >= 0 && i2 == 5)
                              k2 = j1;
                        if (k2 >= 256 && k2 < 512 && !IdentityKit.kits[k2 - 256].bodyLoaded())
                              flag = true;
                        if (k2 >= 512 && !ItemDefinition.lookup(k2 - 512)
                                    .isEquippedModelCached(gender))
                              flag = true;
                  }

                  if (flag) {
                        if (aLong1697 != -1L)
                              model_1 = (Model) models.get(aLong1697);
                        if (model_1 == null)
                              return null;
                  }
            }
            if (model_1 == null) {
                  Model aclass30_sub2_sub4_sub6s[] = new Model[14];
                  int j2 = 0;
                  for (int l2 = 0; l2 < 12; l2++) {
                        int i3 = equipment[l2];
                        if (k1 >= 0 && l2 == 3)
                              i3 = k1;
                        if (j1 >= 0 && l2 == 5)
                              i3 = j1;
                        if (i3 >= 256 && i3 < 512) {
                              Model model_3 = IdentityKit.kits[i3 - 256].bodyModel();
                              if (model_3 != null)
                                    aclass30_sub2_sub4_sub6s[j2++] = model_3;
                        }
                        if (i3 >= 512) {
                              Model model_4 =
                                          ItemDefinition.lookup(i3 - 512).getEquippedModel(gender);
                              if (model_4 != null)
                                    aclass30_sub2_sub4_sub6s[j2++] = model_4;
                        }
                  }
                  // aclass30_sub2_sub4_sub6s[j2++] = Model.method462(9996);
                  model_1 = new Model(j2, aclass30_sub2_sub4_sub6s);
                  for (int j3 = 0; j3 < 5; j3++)
                        if (appearanceColors[j3] != 0) {
                              model_1.recolor(Client.PLAYER_BODY_RECOLOURS[j3][0],
                                          Client.PLAYER_BODY_RECOLOURS[j3][appearanceColors[j3]]);
                              if (j3 == 1)
                                    model_1.recolor(Client.anIntArray1204[0],
                                                Client.anIntArray1204[appearanceColors[j3]]);
                        }

                  model_1.skin();
                  model_1.scale(132, 132, 132);
                  model_1.light(72, 1300, -30, -50, -30, true);
                  models.put(model_1, l);
                  aLong1697 = l;
            }
            if (aBoolean1699)
                  return model_1;
            Model model_2 = Model.EMPTY_MODEL;
            model_2.method464(model_1, Frame.noAnimationInProgress(currentFrame) & Frame.noAnimationInProgress(i1));
            if (currentFrame != -1 && i1 != -1)
                  model_2.method471(Animation.animations[super.emoteAnimation].interleaveOrder, i1,
                              currentFrame);
            else if (currentFrame != -1 && nextFrame != -1)
                  model_2.method470(currentFrame, nextFrame, cycle1, cycle2);
            else
                  model_2.apply(currentFrame);
            model_2.method466();
            model_2.faceGroups = null;
            model_2.vertexGroups = null;
            return model_2;
      }

      public boolean isVisible() {
            return visible;
      }

      public int privelage;

      public Model method453() {
            if (!visible)
                  return null;
            if (desc != null)
                  return desc.model();
            boolean flag = false;
            for (int i = 0; i < 12; i++) {
                  int j = equipment[i];
                  if (j >= 256 && j < 512 && !IdentityKit.kits[j - 256].headLoaded())
                        flag = true;
                  if (j >= 512 && !ItemDefinition.lookup(j - 512).isDialogueModelCached(gender))
                        flag = true;
            }

            if (flag)
                  return null;
            Model aclass30_sub2_sub4_sub6s[] = new Model[12];
            int k = 0;
            for (int l = 0; l < 12; l++) {
                  int i1 = equipment[l];
                  if (i1 >= 256 && i1 < 512) {
                        Model model_1 = IdentityKit.kits[i1 - 256].headModel();
                        if (model_1 != null)
                              aclass30_sub2_sub4_sub6s[k++] = model_1;
                  }
                  if (i1 >= 512) {
                        Model model_2 = ItemDefinition.lookup(i1 - 512).getChatEquipModel(gender);
                        if (model_2 != null)
                              aclass30_sub2_sub4_sub6s[k++] = model_2;
                  }
            }

            Model model = new Model(k, aclass30_sub2_sub4_sub6s);
            for (int j1 = 0; j1 < 5; j1++)
                  if (appearanceColors[j1] != 0) {
                        model.recolor(Client.PLAYER_BODY_RECOLOURS[j1][0],
                                    Client.PLAYER_BODY_RECOLOURS[j1][appearanceColors[j1]]);
                        if (j1 == 1)
                              model.recolor(Client.anIntArray1204[0],
                                          Client.anIntArray1204[appearanceColors[j1]]);
                  }

            return model;
      }

      public Player() {
            aLong1697 = -1L;
            aBoolean1699 = false;
            appearanceColors = new int[5];
            visible = false;
            equipment = new int[12];
      }

      private long aLong1697;
      public NpcDefinition desc;
      public boolean aBoolean1699;
      public final int[] appearanceColors;
      public int team;
      private int gender;
      public String name;
      public static ReferenceCache models = new ReferenceCache(260);
      public int combatLevel;
      public int headIcon;
      public int skullIcon;
      public int hintIcon;
      public int objectModelStart;
      public int objectModelStop;
      public int anInt1709;
      public boolean visible;
      public int objectXPos;
      public int objectCenterHeight;
      public int objectYPos;
      public Model objectModel;
      public final int[] equipment;
      private long aLong1718;
      public int objectAnInt1719LesserXLoc;
      public int objectAnInt1720LesserYLoc;
      public int objectAnInt1721GreaterXLoc;
      public int objectAnInt1722GreaterYLoc;
      public int skill;

}
