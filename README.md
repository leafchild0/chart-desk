# chart-desk
Helm charts registry and MS suite

[![auth CI](https://github.com/leafchild0/chart-desk/actions/workflows/auth.yml/badge.svg)](https://github.com/leafchild0/chart-desk/actions/workflows/auth.yml)
[![charts CI](https://github.com/leafchild0/chart-desk/actions/workflows/charts.yml/badge.svg)](https://github.com/leafchild0/chart-desk/actions/workflows/charts.yml)
[![gateway CI](https://github.com/leafchild0/chart-desk/actions/workflows/gateway.yml/badge.svg)](https://github.com/leafchild0/chart-desk/actions/workflows/gateway.yml)
[![ui CI](https://github.com/leafchild0/chart-desk/actions/workflows/ui.yml/badge.svg)](https://github.com/leafchild0/chart-desk/actions/workflows/ui.yml)

## Testing info
1. Upload package
1.1 Add repo in helm:
```
helm repo add test http://localhost:8080/
"test" has been added to your repositories
```
1.2 Install helm publish plugin
```
helm plugin install https://github.com/chartmuseum/helm-push.git
```
1.3 Pushing helm chart in repository:
```
helm cm-push mysql-1.6.0.tgz test
```
optional params:
--username=test
--password=test123
--context-path=/<account>
2. Pulling helm chart
```
helm fetch test/mariadb
```
