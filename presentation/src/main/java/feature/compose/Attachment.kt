/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package feature.compose

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.inputmethod.InputContentInfo
import com.google.android.mms.ContentType

data class Attachment(private val uri: Uri? = null, private val inputContent: InputContentInfo? = null) {

    fun getUri(): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            inputContent?.contentUri ?: uri
        } else {
            uri
        }
    }

    fun isGif(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && inputContent != null) {
            inputContent.description.hasMimeType(ContentType.IMAGE_GIF)
        } else {
            context.contentResolver.getType(uri) == ContentType.IMAGE_GIF
        }
    }

}