import operators.Operator
import reasoning.Reasoning

class BootStrap
{

	def objectMapperService

	def init =
	{ servletContext ->
		log.info("working directory: "+ new File(".").getAbsolutePath()+"|" + System.getProperty("user.dir") + "|" + new File(".").getCanonicalPath())

		Reasoning.metaClass.asTree =
		{
			->
			return objectMapperService.asTree(delegate)
		}

		com.vpundit.www.custom.Tree.metaClass.asReasoning =
		{
			->
			return objectMapperService.asReasoning(delegate)
		}

	}
	def destroy =
	{
	}
}
