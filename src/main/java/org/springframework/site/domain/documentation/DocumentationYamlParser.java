package org.springframework.site.domain.documentation;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentationYamlParser {

	public Map<String, List<Project>> parse(InputStream documentationYaml) {
		Map<String, List<Project>> projects = new HashMap<>();

		Map<String, List> projectsYaml = parseYamlIntoMap(documentationYaml);

		for (Map.Entry<String, List> entry : projectsYaml.entrySet()) {
			String category = entry.getKey();
			if (category.equals("discard")) continue;

			List<Project> categoryProjects = new ArrayList<>();
			if (entry.getValue() == null) {
				projects.put(category, categoryProjects);
				continue;
			}

			for (Object value : entry.getValue()) {
				Map<String, Object> projectData = (Map<String, Object>) value;
				categoryProjects.add(buildProject(projectData));
			}
			projects.put(category, categoryProjects);
		}


		return projects;
	}

	private Map<String, List> parseYamlIntoMap(InputStream documentationYaml) {
		Map load = (Map) new Yaml().load(documentationYaml);
		return (Map) load.get("projects");
	}

	private Project buildProject(Map<String, Object> projectData) {
		Project project = new Project();
		project.setId((String) projectData.get("id"));
		project.setName((String) projectData.get("name"));
		project.setReferenceUrl((String) projectData.get("referenceUrl"));
		project.setGithubUrl((String) projectData.get("githubUrl"));
		project.setApiUrl((String) projectData.get("apiUrl"));
		if (projectData.containsKey("supportedVersions")) {
			for (Object value : (List)projectData.get("supportedVersions")) {
				project.getSupportedVersions().add(value.toString());
			}
		}
		return project;
	}
}
