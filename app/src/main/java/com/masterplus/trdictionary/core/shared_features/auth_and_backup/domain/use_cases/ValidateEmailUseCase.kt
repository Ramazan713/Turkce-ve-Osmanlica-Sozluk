package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
import java.util.regex.Matcher
import java.util.regex.Pattern


class ValidateEmailUseCase {

    operator fun invoke(email: String?): UiText?{
        if(email == null) return null
        if(email.isBlank()){
            return UiText.Resource(R.string.email_field_can_not_be_empty)
        }
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return if(matcher.matches()) null else UiText.Resource(R.string.enter_valid_email)
    }
}