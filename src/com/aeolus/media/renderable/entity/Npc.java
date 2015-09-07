package com.aeolus.media.renderable.entity;
import com.aeolus.cache.def.NpcDefinition;
import com.aeolus.cache.def.SpotAnimation;
import com.aeolus.cache.media.SequenceFrame;
import com.aeolus.media.Animation;
import com.aeolus.media.renderable.Model;

public final class Npc extends Entity
{

	private Model method450()
	{
		if(super.emoteAnimation >= 0 && super.anInt1529 == 0)
		{
			int k = Animation.anims[super.emoteAnimation].anIntArray353[super.anInt1527];
			int i1 = -1;
			if(super.anInt1517 >= 0 && super.anInt1517 != super.standAnimIndex)
				i1 = Animation.anims[super.anInt1517].anIntArray353[super.anInt1518];
			return desc.getAnimatedModel(i1, k, Animation.anims[super.emoteAnimation].anIntArray357);
		}
		int l = -1;
		if(super.anInt1517 >= 0)
			l = Animation.anims[super.anInt1517].anIntArray353[super.anInt1518];
		return desc.getAnimatedModel(-1, l, null);
	}

	public Model getRotatedModel()
	{
		if(desc == null)
			return null;
		Model model = method450();
		if(model == null)
			return null;
		super.height = model.modelHeight;
		if(super.gfxId != -1 && super.anInt1521 != -1)
		{
			SpotAnimation spotAnim = SpotAnimation.cache[super.gfxId];
			Model model_1 = spotAnim.getModel();
			if(model_1 != null)
			{
				int j = spotAnim.animationSequence.anIntArray353[super.anInt1521];
				Model model_2 = new Model(true, SequenceFrame.method532(j), false, model_1);
				model_2.translate(0, -super.anInt1524, 0);
				model_2.prepareSkeleton();
				model_2.method470(j);
				model_2.anIntArrayArray1658 = null;
				model_2.anIntArrayArray1657 = null;
				if(spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
					model_2.scale(spotAnim.resizeXY, spotAnim.resizeXY, spotAnim.resizeZ);
				model_2.light(64 + spotAnim.modelBrightness, 850 + spotAnim.modelShadow, -30, -50, -30, true);
				Model aModel[] = {
						model, model_2
				};
				model = new Model(aModel);
			}
		}
		if(desc.boundDim == 1)
			model.fits_on_single_square = true;
		return model;
	}

	public boolean isVisible()
	{
		return desc != null;
	}

	public Npc()
	{
	}

	public NpcDefinition desc;
}
