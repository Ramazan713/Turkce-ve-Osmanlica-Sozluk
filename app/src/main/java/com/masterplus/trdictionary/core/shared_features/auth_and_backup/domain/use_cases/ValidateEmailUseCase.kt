package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases

import android.util.Patterns
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.UiText

class ValidateEmailUseCase {

    operator fun invoke(email: String?): UiText?{
        if(email == null) return null
        if(email.isBlank()){
            return UiText.Resource(R.string.email_field_can_not_be_empty)
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).find())
            return UiText.Resource(R.string.enter_valid_email)
        return null
    }

}