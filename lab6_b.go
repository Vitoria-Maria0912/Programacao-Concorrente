package main

import (
	"fmt"
	"math/rand"
	"time"
)

func gateway(ngo, wait_n int) int {
	rand.Seed(42)
	for i := 0; i < 10000; i++ {
		v := rand.Intn(100)
		ch <-v //colocando no canal
	}
	close(ch)
}


func main() {

	ch := make(chan int)

	s := gateway(ngo, wait_n int)	
}