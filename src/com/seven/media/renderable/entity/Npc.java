package com.seven.media.renderable.entity;

import com.seven.cache.def.NpcDefinition;
import com.seven.cache.def.SpotAnimation;
import com.seven.cache.media.SequenceFrame;
import com.seven.media.Animation;
import com.seven.media.renderable.Model;

public final class Npc extends Entity {

	public NpcDefinition desc;

	private Model getAnimatedModel() {
		if (super.emoteAnimation >= 0 && super.anInt1529 == 0) {
			int emote = Animation.animations[super.emoteAnimation].anIntArray353[super.anInt1527];
			int movement = -1;
			if (super.movementAnimation >= 0 && super.movementAnimation != super.standAnimIndex)
				movement = Animation.animations[super.movementAnimation].anIntArray353[super.displayedMovementFrames];
			return desc.getAnimatedModel(movement, emote,
					Animation.animations[super.emoteAnimation].anIntArray357);
		}
		int movement = -1;
		if (super.movementAnimation >= 0)
			movement = Animation.animations[super.movementAnimation].anIntArray353[super.displayedMovementFrames];
		return desc.getAnimatedModel(-1, movement, null);
	}

	public Model getRotatedModel() {
		if (desc == null)
			return null;
		Model animatedModel = getAnimatedModel();
		if (animatedModel == null)
			return null;
		super.height = animatedModel.modelHeight;
		if (super.gfxId != -1 && super.anInt1521 != -1) {
			SpotAnimation spotAnim = SpotAnimation.cache[super.gfxId];
			Model graphicModel = spotAnim.getModel();
			if (graphicModel != null) {
				int frame = spotAnim.animationSequence.anIntArray353[super.anInt1521];
				Model model = new Model(true, SequenceFrame.method532(frame),
						false, graphicModel);
				model.translate(0, -super.anInt1524, 0);
				model.skin();
				model.apply(frame);
				model.faceGroups = null;
				model.vertexGroups = null;
				if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
					model.scale(spotAnim.resizeXY, spotAnim.resizeXY,
							spotAnim.resizeZ);
				model.light(64 + spotAnim.modelBrightness,
						850 + spotAnim.modelShadow, -30, -50, -30, true);
				Model models[] = { animatedModel, model };
				animatedModel = new Model(models);
			}
		}
		if (desc.boundDim == 1)
			animatedModel.fits_on_single_square = true;
		return animatedModel;
	}

	public boolean isVisible() {
		return desc != null;
	}
}
