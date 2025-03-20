package main

import (
	"fmt"
	"math/rand"
	"time"
)

func gateway(ngo, wait_n int, ch chan int) int {
	var totalSum int
	for i := 0; i < ngo; i++ {
		go request(ch)
		time.Sleep(time.Duration(wait_n) * time.Second)
	}
	for j := range ch {
		totalSum += int(j)
	}
	close(ch)
	
	return totalSum
	
}

func request(ch chan int){
	rand.Seed(42)
	n := rand.Intn(100)
	ch <- n
	time.Sleep(time.Duration(n) * time.Second)
}

func main() {
	
	ch := make(chan int)
	wait_n := 3
	ngo := 3
	sum := gateway(ngo, wait_n, ch)	
	
	fmt.Println("Soma total: ", sum)
}