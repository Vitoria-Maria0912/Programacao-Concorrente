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

func consumer(ch chan int) {
	for v :=  range ch{
		if(v %2 ==0){
			fmt.Println("Par:", v)
		}
	}

}

func main() {

	ch := make(chan int)

	go producer(ch)
	go producer(ch)

	go consumer(ch)

	time.Sleep(2 * time.Second) // Tempo suficiente para processar os números
	
}