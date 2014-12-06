package fr.neraud.padlistener.proxy.plugin;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.pad.GetPlayerDataJsonParser;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Thread used to handle processing a call from PAD to Gungho servers
 *
 * @author Neraud
 */
public class ApiCallHandlerThread extends Thread {

	private final Context context;
	private final ApiCallModel callModel;
	private final ListenerService.CaptureListener captureListener;

	public ApiCallHandlerThread(Context context, ApiCallModel callModel, ListenerService.CaptureListener captureListener) {
		this.context = context;
		this.callModel = callModel;
		this.captureListener = captureListener;
	}

	@Override
	public void run() {
		MyLog.entry();

		try {
			switch (callModel.getAction()) {
				case GET_PLAYER_DATA:
					notifyCaptureStarted();

					final GetPlayerDataApiCallResult result = parsePlayerData(callModel);
					savePlayerInfo(result.getPlayerInfo());
					saveMonsters(result.getMonsterCards());
					saveFriends(result.getFriends());

					final JsonCaptureHelper saveHelper = new JsonCaptureHelper(context);
					saveHelper.savePadCapturedData(callModel.getResponseContent());

					final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(context);
					techPrefHelper.setLastCaptureDate(new Date());
					techPrefHelper.setLastCaptureName(result.getPlayerInfo().getName());

					notifyCaptureFinished(result.getPlayerInfo().getName());

					break;
				default:
					MyLog.debug("Ignoring action " + callModel.getAction());
			}
		} catch (final ParsingException e) {
			MyLog.error("parsing error", e);
		}

		MyLog.exit();
	}

	private GetPlayerDataApiCallResult parsePlayerData(ApiCallModel callModel) throws ParsingException {
		MyLog.entry();

		final GetPlayerDataJsonParser parser = new GetPlayerDataJsonParser(context, callModel.getRegion());
		final GetPlayerDataApiCallResult result = parser.parse(callModel.getResponseContent());

		MyLog.exit();
		return result;
	}

	private void savePlayerInfo(CapturedPlayerInfoModel playerInfoModel) {
		MyLog.entry();

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerInfoDescriptor.UriHelper.uriForAll();

		Long fake_id = null;

		final Cursor cursor = cr.query(uri, new String[]{CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName()}, null, null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				fake_id = cursor.getLong(0);
			}
			cursor.close();
		}

		final ContentValues values = CapturedPlayerInfoProviderHelper.modelToValues(playerInfoModel);

		if (fake_id == null) {
			MyLog.debug("Insert new data");
			cr.insert(uri, values);
		} else {
			MyLog.debug("Update existing data");
			cr.update(uri, values, CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " = ?", new String[]{fake_id.toString()});
		}

		MyLog.exit();
	}

	private void saveMonsters(List<MonsterModel> monsters) {
		MyLog.entry();

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerMonsterDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		int i = 0;
		final int count = monsters.size();
		for (final MonsterModel monster : monsters) {
			i++;
			notifySavingMonster(i, count, monster);
			final ContentValues values = CapturedPlayerMonsterProviderHelper.modelToValues(monster);
			cr.insert(uri, values);
		}

		MyLog.exit();
	}

	private void saveFriends(List<CapturedFriendModel> friends) {
		MyLog.entry();

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerFriendDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		int i = 0;
		final int count = friends.size();
		for (final CapturedFriendModel friend : friends) {
			i++;
			notifySavingFriend(i, count, friend);
			final ContentValues values = CapturedPlayerFriendProviderHelper.modelToValues(friend);
			cr.insert(uri, values);
		}

		MyLog.exit();
	}

	private void notifyCaptureStarted() {
		if (captureListener != null) {
			captureListener.notifyCaptureStarted();
		}
	}

	private void notifySavingMonster(int num, int count, MonsterModel monster) {
		if (captureListener != null) {
			captureListener.notifySavingMonsters(num, count, monster);
		}
	}

	private void notifySavingFriend(int num, int count, CapturedFriendModel friend) {
		if (captureListener != null) {
			captureListener.notifySavingFriends(num, count, friend);
		}
	}

	private void notifyCaptureFinished(String playerName) {
		if (captureListener != null) {
			captureListener.notifyCaptureFinished(playerName);
		}
	}

}
