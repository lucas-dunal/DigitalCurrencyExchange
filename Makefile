.PHONY: build spin-local push deploy

IMAGE=hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest

build:
	docker build . -t $(IMAGE)

spin-local: build
	docker rm $$(docker stop $$(docker ps -a -q --filter ancestor=$(IMAGE) --format="{{.ID}}")) || true
	docker run -p 8080\:8080 $(IMAGE)

push: build
	docker image push $(IMAGE)

deploy: push
	cd ./deploy/dce && helm install dce . --set image.repository=$(IMAGE)

