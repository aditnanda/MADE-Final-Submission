/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nand_project.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nand_project.moviecatalogue.data.FavTvShowDAO;
import com.nand_project.moviecatalogue.data.factory.AppDatabase;
import com.nand_project.moviecatalogue.model.FavoritTvShowModel;

import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class TvShowFavContentProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.nand_project.moviecatalogue.provider.tv";

    /** The URI for the Cheese table. */
    public static final Uri URI_FAVTVSHOW = Uri.parse(
            "content://" + AUTHORITY + "/" + FavoritTvShowModel.TABLE_NAME_FAV_TVSHOW);

    /** The match code for some items in the Cheese table. */
    private static final int CODE_FAVMOVIE_DIR = 1;

    /** The match code for an item in the Cheese table. */
    private static final int CODE_FAVMOVIE_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, FavoritTvShowModel.TABLE_NAME_FAV_TVSHOW, CODE_FAVMOVIE_DIR);
        MATCHER.addURI(AUTHORITY, FavoritTvShowModel.TABLE_NAME_FAV_TVSHOW + "/*", CODE_FAVMOVIE_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_FAVMOVIE_DIR || code == CODE_FAVMOVIE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            FavTvShowDAO cheese = AppDatabase.getInstance(context).favTvShowDAO();
            final Cursor cursor;
            if (code == CODE_FAVMOVIE_DIR) {
                cursor = cheese.selectAll();
            } else {
                cursor = cheese.selectbyID(ContentUris.parseId(uri)+"");
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_FAVMOVIE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + FavoritTvShowModel.TABLE_NAME_FAV_TVSHOW;
            case CODE_FAVMOVIE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + FavoritTvShowModel.TABLE_NAME_FAV_TVSHOW;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_FAVMOVIE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = AppDatabase.getInstance(context).favTvShowDAO()
                        .insertData(FavoritTvShowModel.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_FAVMOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAVMOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAVMOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = AppDatabase.getInstance(context).favTvShowDAO()
                        .deleteById(ContentUris.parseId(uri)+"");
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAVMOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAVMOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final FavoritTvShowModel cheese = FavoritTvShowModel.fromContentValues(values);
                cheese.id = ContentUris.parseId(uri)+"";
                final int count = AppDatabase.getInstance(context).favTvShowDAO()
                        .updateData(cheese);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @SuppressWarnings("RedundantThrows") /* This gets propagated up from the Callable */
    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull final ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final AppDatabase database = AppDatabase.getInstance(context);
        return database.runInTransaction(new Callable<ContentProviderResult[]>() {
            @Override
            public ContentProviderResult[] call() throws OperationApplicationException {
                return TvShowFavContentProvider.super.applyBatch(operations);
            }
        });
    }



}
