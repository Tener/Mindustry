package io.anuke.mindustry.ui;

import static io.anuke.mindustry.Vars.ui;

import io.anuke.mindustry.Vars;
import io.anuke.mindustry.core.GameState;
import io.anuke.mindustry.core.GameState.State;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.builders.build;
import io.anuke.ucore.scene.builders.imagebutton;
import io.anuke.ucore.scene.ui.ConfirmDialog;
import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.ui.layout.Cell;

public class MenuDialog extends FloatingDialog{
	private SaveDialog save = new SaveDialog();
	private LoadDialog load = new LoadDialog();
	public boolean wasPaused = false;

	public MenuDialog() {
		super("Paused");
		setup();
	}

	void setup(){
		shown(() -> {
			wasPaused = GameState.is(State.paused);
			GameState.set(State.paused);
		});
		
		if(!Vars.android){
			content().defaults().width(220).height(50);

			content().addButton("$text.back", () -> {
				hide();
				if(!wasPaused)
					GameState.set(State.playing);
			});

			content().row();
			content().addButton("$text.settings", () -> {
				ui.showPrefs();
			});

			if(!Vars.gwt){
				content().row();
				content().addButton("$text.savegame", () -> {
					save.show();
				});

				content().row();
				content().addButton("$text.loadgame", () -> {
					load.show();
				});
			}

			content().row();
			content().addButton("$text.quit", () -> {
				new ConfirmDialog("$text.confirm", "$text.quit.confirm", () -> {
					hide();
					GameState.set(State.menu);
				}){
					{
						for(Cell<?> cell : getButtonTable().getCells())
							cell.pad(3).size(180, 44);
					}
				}.show();
			});

		}else{
			build.begin(content());
			
			PressGroup group = new PressGroup();
			
			content().defaults().size(120f).pad(5);
			float isize = 14f*4;
			
			new imagebutton("icon-play-2", isize, () -> {
				hide();
				if(!wasPaused)
					GameState.set(State.playing);
			}).text("$text.back").padTop(4f);
			
			new imagebutton("icon-tools", isize, () -> ui.showPrefs()).text("$text.settings").padTop(4f);
			
			new imagebutton("icon-save", isize, ()-> save.show()).text("$text.save").padTop(4f);
			
			new imagebutton("icon-load", isize, () -> load.show()).text("$text.load").padTop(4f);
			
			new imagebutton("icon-quit", isize, () -> {
				new ConfirmDialog("$text.confirm", "$text.quit.confirm", () -> {
					hide();
					GameState.set(State.menu);
				}){{
					for(Cell<?> cell : getButtonTable().getCells())
						cell.pad(3).size(180, 44);
				}}.show();
			}).text("Quit").padTop(4f);
			
			for(Element e : content().getChildren()){
				if(e instanceof ImageButton){
					group.add((ImageButton)e);
				}
			}
			
			build.end();
		}
	}
}
