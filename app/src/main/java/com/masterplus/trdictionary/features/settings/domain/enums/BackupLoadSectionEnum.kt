package com.masterplus.trdictionary.features.settings.domain.enums

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class BackupLoadSectionEnum: IMenuItemEnum {

    LoadLastBackup {
        override val title: UiText
            get() = UiText.Resource(R.string.load_last_Backup)
    },
    ShowBackupFiles {
        override val title: UiText
            get() = UiText.Resource(R.string.show_backup_files)
    },
    NotShowAgain {
        override val title: UiText
            get() = UiText.Resource(R.string.not_show_warning_again)
    };

    override val iconInfo: IconInfo?
        get() = null

}