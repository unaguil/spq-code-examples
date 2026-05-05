# Configuration file for the Sphinx documentation builder.
# https://www.sphinx-doc.org/en/master/usage/configuration.html

project   = 'SpringBoot REST MySQL Multimodule'
copyright = '2026, es.deusto.spq'
author    = 'es.deusto.spq'
release   = '0.0.1-SNAPSHOT'

# -- General configuration ---------------------------------------------------

extensions = [
    'sphinx.ext.autosectionlabel',
]

templates_path   = ['_templates']
exclude_patterns = ['_build', 'Thumbs.db', '.DS_Store']

# -- Options for HTML output -------------------------------------------------

html_theme   = 'sphinx_rtd_theme'

html_theme_options = {
    'navigation_depth': 3,
    'titles_only': False,
}
