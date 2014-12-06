package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends AbstractMonsterCursorAdapter {

	public MonsterInfoCursorAdapter(Context context) {
		super(context, R.layout.view_monster_info_item);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		MyLog.entry();

		final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);

		final ImageView monsterImageView = fillImage(view, R.id.view_monster_info_item_image, model);
		monsterImageView.clearColorFilter();

		final ViewGroup monsterTextBlock = (ViewGroup) view.findViewById(R.id.view_monster_info_item_text_block);
		monsterTextBlock.setVisibility(View.INVISIBLE);

		final TextView monsterIdText = (TextView) view.findViewById(R.id.view_monster_info_item_text_id);
		monsterIdText.setText("" + model.getIdJP());

		final TextView monsterNameText = (TextView) view.findViewById(R.id.view_monster_info_item_text_name);
		monsterNameText.setText(model.getName());

		monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				monsterTextBlock.setVisibility(View.VISIBLE);
				monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				MyLog.exit();
			}
		});

		monsterTextBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				monsterTextBlock.setVisibility(View.INVISIBLE);
				monsterImageView.clearColorFilter();
				MyLog.exit();
			}
		});

		MyLog.exit();
	}
}
