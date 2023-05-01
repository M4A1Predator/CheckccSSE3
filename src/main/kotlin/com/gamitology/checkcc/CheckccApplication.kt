package com.gamitology.checkcc

import com.gamitology.checkcc.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.Scheduled


@SpringBootApplication
class CheckccApplication : CommandLineRunner {

	@Autowired
	lateinit var dataService: DataService

	override fun run(vararg args: String?) {
//		val sc = Scanner(System.`in`)
//		sc.hasNext()
	}

	@Scheduled(fixedRate = 8000)
	fun scheduleFixedRateTask() {
		dataService.process()
	}
}

fun main(args: Array<String>) {
	runApplication<CheckccApplication>(*args)
}
