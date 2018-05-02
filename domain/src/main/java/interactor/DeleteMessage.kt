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
package interactor

import io.reactivex.Flowable
import repository.MessageRepository
import javax.inject.Inject

class DeleteMessage @Inject constructor(
        private val messageRepo: MessageRepository,
        private val updateBadge: UpdateBadge
) : Interactor<Long>() {

    override fun buildObservable(params: Long): Flowable<*> {
        return Flowable.just(params)
                .doOnNext { messageId ->
                    val threadId = messageRepo.getMessage(messageId)?.threadId
                    messageRepo.deleteMessage(messageId)
                    threadId?.let { messageRepo.updateConversations(it) }
                }
                .doOnNext { messageRepo.updateConversations(params) } // Update the conversation
                .flatMap { updateBadge.buildObservable(Unit) } // Update the badge
    }

}