# -*- coding: utf-8 -*-
import urllib

def print_random_word():
    print(extract_word(get_content()))


def get_content():
    """
    Функция возвращает содержимое вики-страницы name из русской Википедии.
    В случае ошибки загрузки или отсутствия страницы возвращается None.
    """
    try:
        return urllib.urlopen('https://jimpix.co.uk/generators/word-generator.asp').read()
    except Exception:
        return None


def extract_word(page):
    """
    Функция принимает на вход содержимое страницы
    и возвращает случайное слово
    """
    key = "<span class=\"words2\">"
    char = ""
    word = ""
    char_id = page.find(key) + len(key) - 1
    while char != "<":
        word += char
        char_id += 1
        char = page[char_id]

    return word.capitalize()


if __name__ == '__main__':
    print_random_word()