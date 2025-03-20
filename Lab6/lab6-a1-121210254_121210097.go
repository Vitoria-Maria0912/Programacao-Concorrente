// Matrícula 1: 121210254
// Matrícula 2: 121210097
package main

import (
	"fmt"
	"math/rand"
)

func producer(ch chan int){
	rand.Seed(42)
	for{
		v := rand.Intn(10)
		ch <-v //colocando no canal
	}
}

func consumer(ch chan int){
	for v :=  range ch{
		if(v %2 ==0){
			fmt.Println("Par:", v)
		}
	}

}

func main(){


	ch := make(chan int)

	go producer(ch)
	go consumer(ch)

	select{}
}