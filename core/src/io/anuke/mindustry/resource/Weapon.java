package io.anuke.mindustry.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.mindustry.Vars;
import io.anuke.mindustry.entities.Bullet;
import io.anuke.mindustry.entities.BulletType;
import io.anuke.mindustry.entities.Player;
import io.anuke.mindustry.graphics.Fx;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Bundles;
import io.anuke.ucore.util.Mathf;

public enum Weapon{
	blaster(15, BulletType.shot){
		{
			unlocked = true;
		}
		
		@Override
		public void shoot(Player p){
			super.shoot(p);
			Effects.effect(Fx.shoot3, p.x + vector.x, p.y+vector.y);
		}
	},
	triblaster(13, BulletType.shot, stack(Item.iron, 40)){
		
		@Override
		public void shoot(Player p){
			float ang = mouseAngle(p);
			float space = 12;
			
			bullet(p, p.x, p.y, ang);
			bullet(p, p.x, p.y, ang+space);
			bullet(p, p.x, p.y, ang-space);
			
			Effects.effect(Fx.shoot, p.x + vector.x, p.y+vector.y);
			
		}
	},
	multigun(6, BulletType.multishot, stack(Item.iron, 60), stack(Item.steel, 20)){
		@Override
		public void shoot(Player p){
			float ang = mouseAngle(p);
			MathUtils.random.setSeed(Gdx.graphics.getFrameId());
			
			bullet(p, p.x, p.y, ang + Mathf.range(8));
			
			Effects.effect(Fx.shoot2, p.x + vector.x, p.y+vector.y);
		}
	},
	flamer(5, BulletType.flame, stack(Item.steel, 60), stack(Item.coal, 60)){
		
		{
			shootsound = "flame2";
		}
		
		@Override
		public void shoot(Player p){
			float ang = mouseAngle(p);
			//????
			MathUtils.random.setSeed(Gdx.graphics.getFrameId());
			
			bullet(p, p.x, p.y, ang + Mathf.range(12));
		}
	},
	railgun(40, BulletType.sniper,  stack(Item.steel, 60), stack(Item.iron, 60)){
		
		{
			shootsound = "railgun";
		}
		
		@Override
		public void shoot(Player p){
			float ang = mouseAngle(p);
			
			bullet(p, p.x, p.y, ang);
			Effects.effect(Fx.railshoot, p.x + vector.x, p.y+vector.y);
		}
	},
	mortar(100, BulletType.shell, stack(Item.titanium, 40), stack(Item.steel, 60)){
		
		{
			shootsound = "bigshot";
		}
		
		@Override
		public void shoot(Player p){
			float ang = mouseAngle(p);
			bullet(p, p.x, p.y, ang);
			Effects.effect(Fx.mortarshoot, p.x + vector.x, p.y+vector.y);
			Effects.shake(2f, 2f, Vars.player);
		}
	};
	public float reload;
	public BulletType type;
	public String shootsound = "shoot";
	public boolean unlocked;
	public ItemStack[] requirements;
	public final String description;
	
	Vector2 vector = new Vector2();

	public String localized(){
		return Bundles.get("weapon."+name() + ".name");
	}
	
	private Weapon(float reload, BulletType type, ItemStack... requirements){
		this.reload = reload;
		this.type = type;
		this.requirements = requirements;
		this.description = Bundles.getNotNull("weapon."+name()+".description");
	}

	public void shoot(Player p){
		bullet(p, p.x, p.y, mouseAngle(p));
	}
	
	float mouseAngle(Entity owner){
		return Angles.mouseAngle(owner.x, owner.y);
	}
	
	void bullet(Entity owner, float x, float y, float angle){
		vector.set(3, 0).rotate(mouseAngle(owner));
		new Bullet(type, owner,  x+vector.x, y+vector.y, angle).add();
	}
	
	private static ItemStack stack(Item item, int amount){
		return new ItemStack(item, amount);
	}
}
