package main

import (
	// "fmt"
	"math/rand"
	"time"
)

func exec(tempoDormido int) int {

	time.Sleep(time.Duration(tempoDormido) * time.Second)
	return tempoDormido
}

func aux(max_sleep_ms int) chan int {

	channel := make(chan int)

	for i := 0; i <= 1000; i++ {
		maxTime := rand.Intn(max_sleep_ms)
		channel <- maxTime
		exec(maxTime)
	}
	return channel
}

func main(){

	rand.Seed(42)

	// go aux(max_sleep_ms)
}