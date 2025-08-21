package com.personal.file_sharing_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class FileSharingAppApplication

fun main(args: Array<String>) {
	runApplication<FileSharingAppApplication>(*args)
}
