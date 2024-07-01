package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultIconToggleButton


typealias EmailString = String
typealias PasswordString = String

data class EmailAuthProviderStyles(
    val showSignIn: Boolean = true,
    val showSignUp: Boolean = true,
    val showForgetPassword: Boolean = true
)

@Composable
fun EmailAuthProviderComponent(
    modifier: Modifier = Modifier,
    onSignIn: ((EmailString, PasswordString) -> Unit)? = null,
    onSignUp: ((EmailString, PasswordString) -> Unit)? = null,
    onForgetPassword: ((EmailString) -> Unit )? = null,
    providerStyles: EmailAuthProviderStyles = EmailAuthProviderStyles()
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        EmailField(
            modifier = Modifier.fillMaxWidth(),
            email = email,
            onEmailChange = { email = it }
        )

        PasswordField(
            modifier = Modifier.fillMaxWidth(),
            password = password,
            onPasswordChange = { password = it }
        )

        if(providerStyles.showForgetPassword){
            ForgetPassword(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onForgetPassword?.invoke(email)
                }
            )
        }else{
            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if(providerStyles.showSignUp){
                AuthButton(
                    title = stringResource(id = R.string.sign_up_c),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onSignUp?.invoke(email, password)
                    }
                )
            }
            
            if(providerStyles.showSignIn){
                AuthButton(
                    title = stringResource(id = R.string.sign_in_c),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onSignIn?.invoke(email, password)
                    }
                )
            }
        }
    }

}

@Composable
private fun ForgetPassword(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)?
){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        TextButton(
            onClick = {
                onClick?.invoke()
            }
        ) {
            Text(
                text = stringResource(id = R.string.forget_password),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    val context = LocalContext.current
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = modifier
            .semantics {
                contentDescription = context.getString(R.string.password)
            },
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(text = stringResource(id = R.string.password)) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Password, contentDescription = stringResource(id = R.string.password))
        },
        visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            DefaultIconToggleButton(
                value = showPassword,
                onValueChange = { showPassword = it },
                imageVector = Icons.Default.VisibilityOff,
                selectedImageVector = Icons.Default.Visibility,
            )
        }
    )
}

@Composable
private fun EmailField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        modifier = modifier
            .semantics {
                contentDescription = context.getString(R.string.email)
            },
        value = email,
        onValueChange = onEmailChange,
        label = { Text(text = stringResource(id = R.string.email)) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(id = R.string.email))
        }
    )
}






@Preview(showBackground = true)
@Composable
fun EmailAuthProviderComponentPreview() {
    EmailAuthProviderComponent(
        providerStyles = EmailAuthProviderStyles(
            showSignUp = true
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun EmailAuthProviderComponentPreview2() {
    EmailAuthProviderComponent(
        providerStyles = EmailAuthProviderStyles(
            showSignUp = false
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun EmailAuthProviderComponentPreview3() {
    EmailAuthProviderComponent(
        providerStyles = EmailAuthProviderStyles(
            showSignUp = false,
            showForgetPassword = false
        ),
    )
}