"""Combine the signed-in overview build with the current site's other routes."""
import hashlib
import json
import shutil
import sys
from html.parser import HTMLParser
from pathlib import Path


class Entry(HTMLParser):
    def __init__(self, directory):
        super().__init__()
        self.directory = directory
        self.styles = []
        self.scripts = []
        self.inline = False
        self.feed((directory / 'index.html').read_text())
        if not self.styles or not any('src' in script for script in self.scripts):
            raise ValueError(f'Expected a Vue CLI entry in {directory}')
        for asset in self.styles + [s['src'] for s in self.scripts if 'src' in s]:
            if not asset.startswith('static/') or not (directory / asset).is_file():
                raise ValueError(f'Missing or unexpected entry asset: {asset}')

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        if tag == 'link' and attrs.get('rel') == 'stylesheet':
            self.styles.append(attrs['href'])
        if tag == 'script':
            self.inline = 'src' not in attrs
            self.scripts.append({'code': ''} if self.inline else {'src': attrs['src']})

    def handle_endtag(self, tag):
        if tag == 'script':
            self.inline = False

    def handle_data(self, data):
        if self.inline:
            self.scripts[-1]['code'] += data

    def assets(self):
        return {'styles': self.styles, 'scripts': self.scripts}


def compose(baseline, build, target):
    original, updated = Entry(baseline), Entry(build)
    # The old and new Webpack runtimes share hashed files, but never a document.
    target.mkdir()
    shutil.copytree(baseline, target, dirs_exist_ok=True)
    for source in (build / 'static').rglob('*'):
        if source.is_file():
            dest = target / source.relative_to(build)
            if dest.exists():
                if source.read_bytes() != dest.read_bytes():
                    raise ValueError(f'Asset collision: {dest.name}')
            else:
                dest.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(source, dest)
    config = json.dumps([original.assets(), updated.assets()], ensure_ascii=True)
    script = (Path(__file__).with_name('screen-entry.js')).read_text().replace('/* BUILD_ENTRIES */ []', config)
    name = 'screen-entry.' + hashlib.sha256(script.encode()).hexdigest()[:12] + '.js'
    (target / name).write_text(script)
    (target / 'index.html').write_text(
        '<!doctype html><html lang="zh-CN"><head><meta charset="utf-8">'
        '<meta name="viewport" content="width=device-width,initial-scale=1">'
        '<title>新能智维</title><link rel="icon" href="./favicon.ico"></head>'
        '<body><div id="app"></div><script src="./' + name + '"></script></body></html>'
    )


if __name__ == '__main__':
    compose(*(Path(arg) for arg in sys.argv[1:]))
