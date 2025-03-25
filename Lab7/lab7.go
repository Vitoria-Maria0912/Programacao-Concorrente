package main

import (
	// "fmt"
	"math/rand"
	"time"
)

func exec(maxTime int) int {

	tempoDormido := rand.Intn(maxTime)

	time.Sleep(time.Duration(tempoDormido) * time.Millisecond) 
	return tempoDormido
}

func aux(max_sleep_ms int) chan int {

	channel := make(chan int)

	go func() {
		for i := 0; i <= 1000; i++ {
			tempoDormido := exec(max_sleep_ms)
			channel <- tempoDormido		
		} 
		close(channel)
	}()
	return channel
}

func main() {

	rand.Seed(42)
	max_sleep_ms1 := rand.Intn(10)
	max_sleep_ms2 := rand.Intn(10)

	ch1 := aux(max_sleep_ms1)
	ch2 := aux(max_sleep_ms2)

	for i := 0; i <= 500; i++ {
		
		select {
			case <- ch1:
			case <- ch2:
		}
	}
}