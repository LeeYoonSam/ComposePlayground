package com.ys.composeplayground.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

class MyTypography(
    val headline1BoldSmall: TextStyle = MyTypographyTokens.Headline1BoldSmall,
    val headline1RegularSmall: TextStyle = MyTypographyTokens.Headline1RegularSmall,
    val headline2BoldSmall: TextStyle = MyTypographyTokens.Headline2BoldSmall,
    val headline2RegularSmall: TextStyle = MyTypographyTokens.Headline2RegularSmall,
    val headline3BoldSmall: TextStyle = MyTypographyTokens.Headline3BoldSmall,
    val headline3RegularSmall: TextStyle = MyTypographyTokens.Headline3RegularSmall,
    val subtitle1BoldSmall: TextStyle = MyTypographyTokens.Subtitle1BoldSmall,
    val subtitle1BoldMedium: TextStyle = MyTypographyTokens.Subtitle1BoldMedium,
    val subtitle1RegularSmall: TextStyle = MyTypographyTokens.Subtitle1RegularSmall,
    val subtitle1RegularMedium: TextStyle = MyTypographyTokens.Subtitle1RegularMedium,
    val subtitle2BoldSmall: TextStyle = MyTypographyTokens.Subtitle2BoldSmall,
    val subtitle2BoldMedium: TextStyle = MyTypographyTokens.Subtitle2BoldMedium,
    val subtitle2RegularSmall: TextStyle = MyTypographyTokens.Subtitle2RegularSmall,
    val subtitle2RegularMedium: TextStyle = MyTypographyTokens.Subtitle2RegularMedium,
    val body1BoldSmall: TextStyle = MyTypographyTokens.Body1BoldSmall,
    val body1BoldMedium: TextStyle = MyTypographyTokens.Body1BoldMedium,
    val body1RegularSmall: TextStyle = MyTypographyTokens.Body1RegularSmall,
    val body1RegularMedium: TextStyle = MyTypographyTokens.Body1RegularMedium,
    val body2BoldSmall: TextStyle = MyTypographyTokens.Body2BoldSmall,
    val body2BoldMedium: TextStyle = MyTypographyTokens.Body2BoldMedium,
    val body2RegularSmall: TextStyle = MyTypographyTokens.Body2RegularSmall,
    val body2RegularMedium: TextStyle = MyTypographyTokens.Body2RegularMedium,
    val body3BoldSmall: TextStyle = MyTypographyTokens.Body3BoldSmall,
    val body3BoldMedium: TextStyle = MyTypographyTokens.Body3BoldMedium,
    val body3RegularSmall: TextStyle = MyTypographyTokens.Body3RegularSmall,
    val body3RegularMedium: TextStyle = MyTypographyTokens.Body3RegularMedium,
    val caption1BoldSmall: TextStyle = MyTypographyTokens.Caption1BoldSmall,
    val caption1BoldMedium: TextStyle = MyTypographyTokens.Caption1BoldMedium,
    val caption1RegularSmall: TextStyle = MyTypographyTokens.Caption1RegularSmall,
    val caption1RegularMedium: TextStyle = MyTypographyTokens.Caption1RegularMedium,
    val caption2BoldSmall: TextStyle = MyTypographyTokens.Caption2BoldSmall,
    val caption2BoldMedium: TextStyle = MyTypographyTokens.Caption2BoldMedium,
    val caption2RegularSmall: TextStyle = MyTypographyTokens.Caption2RegularSmall,
    val caption2RegularMedium: TextStyle = MyTypographyTokens.Caption2RegularMedium,
) {

    /** Returns a copy of this Typography, optionally overriding some of the values. */
    fun copy(
        headline1BoldSmall: TextStyle = this.headline1BoldSmall,
        headline1RegularSmall: TextStyle = this.headline1RegularSmall,
        headline2BoldSmall: TextStyle = this.headline2BoldSmall,
        headline2RegularSmall: TextStyle = this.headline2RegularSmall,
        headline3BoldSmall: TextStyle = this.headline3BoldSmall,
        headline3RegularSmall: TextStyle = this.headline3RegularSmall,
        subtitle1BoldSmall: TextStyle = this.subtitle1BoldSmall,
        subtitle1BoldMedium: TextStyle = this.subtitle1BoldMedium,
        subtitle1RegularSmall: TextStyle = this.subtitle1RegularSmall,
        subtitle1RegularMedium: TextStyle = this.subtitle1RegularMedium,
        subtitle2BoldSmall: TextStyle = this.subtitle2BoldSmall,
        subtitle2BoldMedium: TextStyle = this.subtitle2BoldMedium,
        subtitle2RegularSmall: TextStyle = this.subtitle2RegularSmall,
        subtitle2RegularMedium: TextStyle = this.subtitle2RegularMedium,
        body1BoldSmall: TextStyle = this.body1BoldSmall,
        body1BoldMedium: TextStyle = this.body1BoldMedium,
        body1RegularSmall: TextStyle = this.body1RegularSmall,
        body1RegularMedium: TextStyle = this.body1RegularMedium,
        body2BoldSmall: TextStyle = this.body2BoldSmall,
        body2BoldMedium: TextStyle = this.body2BoldMedium,
        body2RegularSmall: TextStyle = this.body2RegularSmall,
        body2RegularMedium: TextStyle = this.body2RegularMedium,
        body3BoldSmall: TextStyle = this.body3BoldSmall,
        body3BoldMedium: TextStyle = this.body3BoldMedium,
        body3RegularSmall: TextStyle = this.body3RegularSmall,
        body3RegularMedium: TextStyle = this.body3RegularMedium,
        caption1BoldSmall: TextStyle = this.caption1BoldSmall,
        caption1BoldMedium: TextStyle = this.caption1BoldMedium,
        caption1RegularSmall: TextStyle = this.caption1RegularSmall,
        caption1RegularMedium: TextStyle = this.caption1RegularMedium,
        caption2BoldSmall: TextStyle = this.caption2BoldSmall,
        caption2BoldMedium: TextStyle = this.caption2BoldMedium,
        caption2RegularSmall: TextStyle = this.caption2RegularSmall,
        caption2RegularMedium: TextStyle = this.caption2RegularMedium,
    ): MyTypography =
        MyTypography(
            headline1BoldSmall = headline1BoldSmall,
            headline1RegularSmall = headline1RegularSmall,
            headline2BoldSmall = headline2BoldSmall,
            headline2RegularSmall = headline2RegularSmall,
            headline3BoldSmall = headline3BoldSmall,
            headline3RegularSmall = headline3RegularSmall,
            subtitle1BoldSmall = subtitle1BoldSmall,
            subtitle1BoldMedium = subtitle1BoldMedium,
            subtitle1RegularSmall = subtitle1RegularSmall,
            subtitle1RegularMedium = subtitle1RegularMedium,
            subtitle2BoldSmall = subtitle2BoldSmall,
            subtitle2BoldMedium = subtitle2BoldMedium,
            subtitle2RegularSmall = subtitle2RegularSmall,
            subtitle2RegularMedium = subtitle2RegularMedium,
            body1BoldSmall = body1BoldSmall,
            body1BoldMedium = body1BoldMedium,
            body1RegularSmall = body1RegularSmall,
            body1RegularMedium = body1RegularMedium,
            body2BoldSmall = body2BoldSmall,
            body2BoldMedium = body2BoldMedium,
            body2RegularSmall = body2RegularSmall,
            body2RegularMedium = body2RegularMedium,
            body3BoldSmall = body3BoldSmall,
            body3BoldMedium = body3BoldMedium,
            body3RegularSmall = body3RegularSmall,
            body3RegularMedium = body3RegularMedium,
            caption1BoldSmall = caption1BoldSmall,
            caption1BoldMedium = caption1BoldMedium,
            caption1RegularSmall = caption1RegularSmall,
            caption1RegularMedium = caption1RegularMedium,
            caption2BoldSmall = caption2BoldSmall,
            caption2BoldMedium = caption2BoldMedium,
            caption2RegularSmall = caption2RegularSmall,
            caption2RegularMedium = caption2RegularMedium,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MyTypography) return false

        if (headline1BoldSmall != other.headline1BoldSmall) return false
        if (headline1RegularSmall != other.headline1RegularSmall) return false
        if (headline2BoldSmall != other.headline2BoldSmall) return false
        if (headline2RegularSmall != other.headline2RegularSmall) return false
        if (headline3BoldSmall != other.headline3BoldSmall) return false
        if (headline3RegularSmall != other.headline3RegularSmall) return false
        if (subtitle1BoldSmall != other.subtitle1BoldSmall) return false
        if (subtitle1BoldMedium != other.subtitle1BoldMedium) return false
        if (subtitle1RegularSmall != other.subtitle1RegularSmall) return false
        if (subtitle1RegularMedium != other.subtitle1RegularMedium) return false
        if (subtitle2BoldSmall != other.subtitle2BoldSmall) return false
        if (subtitle2BoldMedium != other.subtitle2BoldMedium) return false
        if (subtitle2RegularSmall != other.subtitle2RegularSmall) return false
        if (subtitle2RegularMedium != other.subtitle2RegularMedium) return false
        if (body1BoldSmall != other.body1BoldSmall) return false
        if (body1BoldMedium != other.body1BoldMedium) return false
        if (body1RegularSmall != other.body1RegularSmall) return false
        if (body1RegularMedium != other.body1RegularMedium) return false
        if (body2BoldSmall != other.body2BoldSmall) return false
        if (body2BoldMedium != other.body2BoldMedium) return false
        if (body2RegularSmall != other.body2RegularSmall) return false
        if (body2RegularMedium != other.body2RegularMedium) return false
        if (body3BoldSmall != other.body3BoldSmall) return false
        if (body3BoldMedium != other.body3BoldMedium) return false
        if (body3RegularSmall != other.body3RegularSmall) return false
        if (body3RegularMedium != other.body3RegularMedium) return false
        if (caption1BoldSmall != other.caption1BoldSmall) return false
        if (caption1BoldMedium != other.caption1BoldMedium) return false
        if (caption1RegularSmall != other.caption1RegularSmall) return false
        if (caption1RegularMedium != other.caption1RegularMedium) return false
        if (caption2BoldSmall != other.caption2BoldSmall) return false
        if (caption2BoldMedium != other.caption2BoldMedium) return false
        if (caption2RegularSmall != other.caption2RegularSmall) return false
        if (caption2RegularMedium != other.caption2RegularMedium) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headline1BoldSmall.hashCode()
        result = 31 * result + headline1RegularSmall.hashCode()
        result = 31 * result + headline2BoldSmall.hashCode()
        result = 31 * result + headline2RegularSmall.hashCode()
        result = 31 * result + headline3BoldSmall.hashCode()
        result = 31 * result + headline3RegularSmall.hashCode()
        result = 31 * result + subtitle1BoldSmall.hashCode()
        result = 31 * result + subtitle1BoldMedium.hashCode()
        result = 31 * result + subtitle1RegularSmall.hashCode()
        result = 31 * result + subtitle1RegularMedium.hashCode()
        result = 31 * result + subtitle2BoldSmall.hashCode()
        result = 31 * result + subtitle2BoldMedium.hashCode()
        result = 31 * result + subtitle2RegularSmall.hashCode()
        result = 31 * result + subtitle2RegularMedium.hashCode()
        result = 31 * result + body1BoldSmall.hashCode()
        result = 31 * result + body1BoldMedium.hashCode()
        result = 31 * result + body1RegularSmall.hashCode()
        result = 31 * result + body1RegularMedium.hashCode()
        result = 31 * result + body2BoldSmall.hashCode()
        result = 31 * result + body2BoldMedium.hashCode()
        result = 31 * result + body2RegularSmall.hashCode()
        result = 31 * result + body2RegularMedium.hashCode()
        result = 31 * result + body3BoldSmall.hashCode()
        result = 31 * result + body3BoldMedium.hashCode()
        result = 31 * result + body3RegularSmall.hashCode()
        result = 31 * result + body3RegularMedium.hashCode()
        result = 31 * result + caption1BoldSmall.hashCode()
        result = 31 * result + caption1BoldMedium.hashCode()
        result = 31 * result + caption1RegularSmall.hashCode()
        result = 31 * result + caption1RegularMedium.hashCode()
        result = 31 * result + caption2BoldSmall.hashCode()
        result = 31 * result + caption2BoldMedium.hashCode()
        result = 31 * result + caption2RegularSmall.hashCode()
        result = 31 * result + caption2RegularMedium.hashCode()

        return result
    }

    override fun toString(): String {
        return """
            MyTypography(
                headline1BoldSmall: $headline1BoldSmall,
                headline1RegularSmall: $headline1RegularSmall,
                headline2BoldSmall: $headline2BoldSmall,
                headline2RegularSmall: $headline2RegularSmall,
                headline3BoldSmall: $headline3BoldSmall,
                headline3RegularSmall: $headline3RegularSmall,
                subtitle1BoldSmall: $subtitle1BoldSmall,
                subtitle1BoldMedium: $subtitle1BoldMedium,
                subtitle1RegularSmall: $subtitle1RegularSmall,
                subtitle1RegularMedium: $subtitle1RegularMedium,
                subtitle2BoldSmall: $subtitle2BoldSmall,
                subtitle2BoldMedium: $subtitle2BoldMedium,
                subtitle2RegularSmall: $subtitle2RegularSmall,
                subtitle2RegularMedium: $subtitle2RegularMedium,
                body1BoldSmall: $body1BoldSmall,
                body1BoldMedium: $body1BoldMedium,
                body1RegularSmall: $body1RegularSmall,
                body1RegularMedium: $body1RegularMedium,
                body2BoldSmall: $body2BoldSmall,
                body2BoldMedium: $body2BoldMedium,
                body2RegularSmall: $body2RegularSmall,
                body2RegularMedium: $body2RegularMedium,
                body3BoldSmall: $body3BoldSmall,
                body3BoldMedium: $body3BoldMedium,
                body3RegularSmall: $body3RegularSmall,
                body3RegularMedium: $body3RegularMedium,
                caption1BoldSmall: $caption1BoldSmall,
                caption1BoldMedium: $caption1BoldMedium,
                caption1RegularSmall: $caption1RegularSmall,
                caption1RegularMedium: $caption1RegularMedium,
                caption2BoldSmall: $caption2BoldSmall,
                caption2BoldMedium: $caption2BoldMedium,
                caption2RegularSmall: $caption2RegularSmall,
                caption2RegularMedium: $caption2RegularMedium,
            )
        """.trimIndent()
    }
}

/**
 * Helper function for component typography tokens.
 */
internal fun MyTypography.fromToken(value: MyTypographyKeyTokens): TextStyle {
    return when (value) {
        MyTypographyKeyTokens.Headline1BoldSmall -> headline1BoldSmall
        MyTypographyKeyTokens.Headline1RegularSmall -> headline1RegularSmall
        MyTypographyKeyTokens.Headline2BoldSmall -> headline2BoldSmall
        MyTypographyKeyTokens.Headline2RegularSmall -> headline2RegularSmall
        MyTypographyKeyTokens.Headline3BoldSmall -> headline3BoldSmall
        MyTypographyKeyTokens.Headline3RegularSmall -> headline3RegularSmall
        MyTypographyKeyTokens.Subtitle1BoldSmall -> subtitle1BoldSmall
        MyTypographyKeyTokens.Subtitle1BoldMedium -> subtitle1BoldMedium
        MyTypographyKeyTokens.Subtitle1RegularSmall -> subtitle1RegularSmall
        MyTypographyKeyTokens.Subtitle1RegularMedium -> subtitle1RegularMedium
        MyTypographyKeyTokens.Subtitle2BoldSmall -> subtitle2BoldSmall
        MyTypographyKeyTokens.Subtitle2BoldMedium -> subtitle2BoldMedium
        MyTypographyKeyTokens.Subtitle2RegularSmall -> subtitle2RegularSmall
        MyTypographyKeyTokens.Subtitle2RegularMedium -> subtitle2RegularMedium
        MyTypographyKeyTokens.Body1BoldSmall -> body1BoldSmall
        MyTypographyKeyTokens.Body1BoldMedium -> body1BoldMedium
        MyTypographyKeyTokens.Body1RegularSmall -> body1RegularSmall
        MyTypographyKeyTokens.Body1RegularMedium -> body1RegularMedium
        MyTypographyKeyTokens.Body2BoldSmall -> body2BoldSmall
        MyTypographyKeyTokens.Body2BoldMedium -> body2BoldMedium
        MyTypographyKeyTokens.Body2RegularSmall -> body2RegularSmall
        MyTypographyKeyTokens.Body2RegularMedium -> body2RegularMedium
        MyTypographyKeyTokens.Body3BoldSmall -> body3BoldSmall
        MyTypographyKeyTokens.Body3BoldMedium -> body3BoldMedium
        MyTypographyKeyTokens.Body3RegularSmall -> body3RegularSmall
        MyTypographyKeyTokens.Body3RegularMedium -> body3RegularMedium
        MyTypographyKeyTokens.Caption1BoldSmall -> caption1BoldSmall
        MyTypographyKeyTokens.Caption1BoldMedium -> caption1BoldMedium
        MyTypographyKeyTokens.Caption1RegularSmall -> caption1RegularSmall
        MyTypographyKeyTokens.Caption1RegularMedium -> caption1RegularMedium
        MyTypographyKeyTokens.Caption2BoldSmall -> caption2BoldSmall
        MyTypographyKeyTokens.Caption2BoldMedium -> caption2BoldMedium
        MyTypographyKeyTokens.Caption2RegularSmall -> caption2RegularSmall
        MyTypographyKeyTokens.Caption2RegularMedium -> caption2RegularMedium
    }
}

internal enum class MyTypographyKeyTokens {
    Headline1BoldSmall,
    Headline1RegularSmall,
    Headline2BoldSmall,
    Headline2RegularSmall,
    Headline3BoldSmall,
    Headline3RegularSmall,
    Subtitle1BoldSmall,
    Subtitle1BoldMedium,
    Subtitle1RegularSmall,
    Subtitle1RegularMedium,
    Subtitle2BoldSmall,
    Subtitle2BoldMedium,
    Subtitle2RegularSmall,
    Subtitle2RegularMedium,
    Body1BoldSmall,
    Body1BoldMedium,
    Body1RegularSmall,
    Body1RegularMedium,
    Body2BoldSmall,
    Body2BoldMedium,
    Body2RegularSmall,
    Body2RegularMedium,
    Body3BoldSmall,
    Body3BoldMedium,
    Body3RegularSmall,
    Body3RegularMedium,
    Caption1BoldSmall,
    Caption1BoldMedium,
    Caption1RegularSmall,
    Caption1RegularMedium,
    Caption2BoldSmall,
    Caption2BoldMedium,
    Caption2RegularSmall,
    Caption2RegularMedium,
}

internal val LocalTypography = staticCompositionLocalOf { MyTypography() }
