package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.Constants;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;

public class GravitySource extends Component{
	/**
	 * must have {@link Pos.class}
	 */
	@EntityId public int originEntity = -1;
	
	public float sphereOfInfluence;
	public float effectiveGravity;
	
	public Offset offset = Offset.NONE;
	
	public Vector2 getOrigin(
			ComponentMapper<Pos> positionMapper,
			ComponentMapper<Angle> angleMapper){
		Angle parentAngle = angleMapper.getSafe(originEntity, Angle.NONE);		
		Pos parentPos = positionMapper.getSafe(originEntity, Constants.EMPTY_POS);
		
		return offset.xy.cpy().rotate(parentAngle.rotation).add(parentPos.xy);

	}
}
