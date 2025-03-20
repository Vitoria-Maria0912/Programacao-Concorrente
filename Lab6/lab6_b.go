package main

import (
	"fmt"
	"math/rand"
	"time"
)

func gateway(ngo, wait_n int, ch chan int) int {
	var totalSum int64
	for i := 0; i < ngo; i++ {
		go request(ch)
		time.Sleep(wait_n)
	}
	for j := range ch {
		totalSum += int64(j)
	}
	return totalSum
	
}

func request(ch chan int){
	rand.Seed(42)
	n := rand.Intn(100)
	ch <- n
	fmt.Println("chegou viu  ")
	time.Sleep(n)
}

func main() {

	ch := make(chan int)
	wait_n := 3
	ngo := 3
	gateway(ngo, wait_n, ch)	

	time.Sleep(time.Duration(10) * time.Second)
}