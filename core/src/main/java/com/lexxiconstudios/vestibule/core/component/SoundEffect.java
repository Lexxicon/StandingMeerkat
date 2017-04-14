package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.badlogic.gdx.audio.Sound;

public class SoundEffect extends Component{
	public Sound sfx;
	public long id;
	public boolean shouldPlay = false;
	public boolean shouldStop = false;
	public float volume = 1.0f;
}
