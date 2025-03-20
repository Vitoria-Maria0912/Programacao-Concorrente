package main

import (
	"fmt"
	"math/rand"
	"time"
)

func producer(ch chan int) {
	rand.Seed(42)
	n := rand.Intn(1000)
	for i := 0; i < n; i++ {
		v := rand.Intn(100)
		ch <-v //colocando no canal
	}
}

func consumer(in chan int, out chan int) {
	for v :=  range in {
		if(v %2 ==0){
			out <- v //colocando no canal
			fmt.Println("Par:", v)
		}
	}

}

func main() {
	n := 20
	in := make(chan int, n)
	out := make(chan int, n)

	go producer(in)
	go producer(in)

	go consumer(in, out)

	time.Sleep(2 * time.Second) // Tempo suficiente para processar os números
	
}