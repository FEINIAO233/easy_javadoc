package com.star.easydoc.view.settings.kdoc.template

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.ConfigurationException
import com.star.easydoc.config.EasyDocConfigComponent
import com.star.easydoc.view.settings.javadoc.template.AbstractTemplateConfigurable
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * @author [wangchao](mailto:wangchao.star@gmail.com)
 * @version 1.0.0
 * @since 2019-11-10 17:35:00
 */
class KtFieldSettingsConfigurable : AbstractTemplateConfigurable<KtFieldSettingsView>() {
    private val config = ServiceManager.getService(EasyDocConfigComponent::class.java).state!!
    private val ktFieldConfigView = KtFieldSettingsView(config)
    override fun getView(): KtFieldSettingsView {
        return ktFieldConfigView
    }

    override fun getDisplayName(): String {
        return "Field Template"
    }

    override fun isModified(): Boolean {
        val templateConfig = config.kdocFieldTemplateConfig
        if (templateConfig.isDefault != view.isDefault) {
            return true
        }
        return templateConfig.template != view.template
    }

    override fun apply() {
        val templateConfig = config.kdocFieldTemplateConfig
        templateConfig.isDefault = view.isDefault
        templateConfig.template = view.template
        if (templateConfig.customMap == null) {
            templateConfig.customMap = TreeMap()
        }
        if (!view.isDefault) {
            if (StringUtils.isBlank(view.template)) {
                throw ConfigurationException("使用自定义模板，模板不能为空")
            }
            val temp = StringUtils.strip(view.template)
            if (!temp.startsWith("/**") || !temp.endsWith("*/")) {
                throw ConfigurationException("模板格式不正确，正确的kdoc应该以\"/**\"开头，以\"*/\"结束")
            }
        }
    }

    override fun reset() {
        val templateConfig = config.kdocFieldTemplateConfig
        view.isDefault = BooleanUtils.isTrue(templateConfig.isDefault)
        view.template = templateConfig.template
    }
}