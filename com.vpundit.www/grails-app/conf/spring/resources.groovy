import persistentrep.ReasoningXMLLoader
import main.AnalyzeNewPort

// Place your Spring DSL code here
beans = {
	reasoningXMLLoader(ReasoningXMLLoader)
	stockDataCompiler(StockDataCompiler)
	stockDomain(StockDomain)
	stockDataSet(DataSet)
	analyzeNewPort(AnalyzeNewPort)
}
