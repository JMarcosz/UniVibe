package com.example.univibe.domain.use_case.events

import javax.inject.Inject

class ScanQrEventUseCase @Inject constructor() {

    operator fun invoke(qrContent: String): String? {
        val uri = android.net.Uri.parse(qrContent)
        if (uri.scheme == "univibe" && uri.host == "event") {
            return uri.lastPathSegment
        }
        return null
    }
}
