// Matrícula 1: 121210254
// Matrícula 2: 121210097
package main

import (
	"fmt"
	"math/rand"
	"time"
)

func producer(ch chan int) {
	rand.Seed(42)
	for i := 0; i < 10000; i++ {
		v := rand.Intn(100)
		ch <-v //colocando no canal
	}
	close(ch)
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
	go consumer(ch)

	time.Sleep(2 * time.Second) // Tempo suficiente para processar os números
	
}